package com.app.publishsubscribe.service;

import com.app.publishsubscribe.config.exception.SubscriberNotFoundException;
import com.app.publishsubscribe.domain.*;
import com.app.publishsubscribe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PubSubService {

    private final PublisherService publisherService;
    private final SubscriberService subscriberService;
    private final SubscriberRepository subscriberRepository;
    private final MessageRepository messageRepository;

    public void addSubscriber(Subscriber subscriber) {
        subscriberService.addSubscriber(subscriber);
    }

    public void removeSubscriber(Long subscriberId) {
        subscriberService.removeSubscriber(subscriberId);
    }

    public void broadcastMessages(Long id) throws SubscriberNotFoundException {
        subscriberService.listenForMessages(id);
    }

    public List<Message> getMessagesForSubscriber(Long id, String sort, String createdDate, Integer pageNo, Integer pageSize) {
        Subscriber subscriber = subscriberRepository.findById(id).orElse(null);
        if (subscriber == null) {
            throw new IllegalArgumentException("Subscriber not found.");
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<Message> messages = createdDate != null
                ? messageRepository.findAllBySubscriberAndCreatedGreaterThanEqual(subscriber ,LocalDate.parse(createdDate), pageable)
                : messageRepository.findAllBySubscriber(subscriber, pageable);

        if(sort.equals("desc")){
            messages.sort(Comparator.comparing(Message::getCreated).reversed());
        }else {
            messages.sort(Comparator.comparing(Message::getCreated));
        }
        return messages;
    }

    public void addMessageToQueue(Payload payload) {
        Message message = new Message();
        message.setPayload(payload);
        publisherService.setMessageSchedule(message);
    }
}