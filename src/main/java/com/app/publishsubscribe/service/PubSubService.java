package com.app.publishsubscribe.service;

import com.app.publishsubscribe.domain.*;
import com.app.publishsubscribe.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.text.*;
import java.util.*;

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

        List<Message> messages = createdDate != null
                ? messageRepository.findAllBySubscriberAndCreatedGreaterThanEqual(subscriber ,convertStringToDate(createdDate), pageable)
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

    private Date convertStringToDate(String date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        try {
            newDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }
}