package com.app.publishsubscribe.service;

import com.app.publishsubscribe.domain.Message;
import com.app.publishsubscribe.domain.Subscriber;
import com.app.publishsubscribe.repository.MessageRepository;
import com.app.publishsubscribe.repository.SubscriberRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PubSubService {

    private final PublisherService publisherService;
    private final SubscriberService subscriberService;
    private final SubscriberRepository subscriberRepository;
    private final MessageRepository messageRepository;

    public PubSubService(PublisherService publisherService, SubscriberService subscriberService,
                         SubscriberRepository subscriberRepository, MessageRepository messageRepository) {
        this.publisherService = publisherService;
        this.subscriberService = subscriberService;
        this.subscriberRepository = subscriberRepository;
        this.messageRepository = messageRepository;
    }

    public void addSubscriber(Subscriber subscriber) {
        subscriberService.addSubscriber(subscriber);

    }

    public void removeSubscriber(Long subscriberId) {
        subscriberService.removeSubscriber(subscriberId);
    }

    public void broadcastMessages(Subscriber subscriber) {
        subscriberService.listenForMessages(subscriber);
    }
    public List<Message> getMessagesForSubscriber(Long id, String sort, String createdDate, Integer pageNo, Integer pageSize) {
        Subscriber subscriber = subscriberRepository.findById(id).orElse(null);
        if (subscriber == null) {
            throw new IllegalArgumentException("Subscriber not found.");
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<Message> messages = messageRepository.findAllBySubscriber(subscriber, pageable);

        if(sort.equals("desc")){
            messages.sort(Comparator.comparing(Message::getCreated).reversed());
        }else {
            messages.sort(Comparator.comparing(Message::getCreated));
        }
        return messages;
    }

    public void addMessageToQueue(Object payload) {
        Message message = new Message();
        message.setPayload(payload);
        publisherService.messageQueue.offer(message);
    }
}