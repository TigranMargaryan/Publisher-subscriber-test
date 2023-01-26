package com.app.publishsubscribe.service;

import com.app.publishsubscribe.config.exception.EmptyMessageQueueException;
import com.app.publishsubscribe.config.exception.SubscriberNotFoundException;
import com.app.publishsubscribe.domain.*;
import com.app.publishsubscribe.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class SubscriberService {

    private static final Integer MAX_RETRIES = 5;

    private final SubscriberRepository subscriberRepository;

    private final MessageRepository messageRepository;

    private final PayloadRepository payloadRepository;

    public void addSubscriber(Subscriber subscriber) {
        subscriberRepository.save(subscriber);
    }

    public void removeSubscriber(Long subscriberId) {
        checkSubscriber(subscriberId);
        subscriberRepository.deleteById(subscriberId);
    }

    public void listenForMessages(Long id) throws SubscriberNotFoundException {
        Subscriber subscriber = checkSubscriber(id);
        Message message = PublisherService.MESSAGE_QUEUE.poll();
        if (message == null) {
            throw new EmptyMessageQueueException("Message queue was empty");
        }

        while (message != null) {
            boolean success = false;
            int retries = 0;
            while (!success && retries < MAX_RETRIES) {
                try {
                        Payload payload = message.getPayload();
                        payloadRepository.save(payload);
                        message.setSubscriber(subscriber);
                        messageRepository.save(message);
                        subscriberRepository.save(subscriber);
                        success = true;
                        log.info("Successfully saved message for subscriber: {}", subscriber.getName());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    retries++;
                }
            }
            if (!success) {
                log.info("The message could not be processed after MAX_RETRIES retries");
            }
            message = PublisherService.MESSAGE_QUEUE.poll();
        }
    }

    private Subscriber checkSubscriber(Long id) {
        Optional<Subscriber> subscriber = subscriberRepository.findById(id);
        if (subscriber.isEmpty()) {
            throw new SubscriberNotFoundException("subscriber not found");
        }
        return subscriber.get();
    }
}