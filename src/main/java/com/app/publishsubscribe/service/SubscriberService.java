package com.app.publishsubscribe.service;

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
        subscriberRepository.deleteById(subscriberId);
    }

    public void listenForMessages(Subscriber sub) throws SubscriberNotFoundException {
        Message message = PublisherService.MESSAGE_QUEUE.poll();

        while (message != null) {

            boolean success = false;
            int retries = 0;
            while (!success && retries < MAX_RETRIES) {
                Optional<Subscriber> subscriber = subscriberRepository.findByName(sub.getName());
                if (subscriber.isEmpty()) {
                    throw new SubscriberNotFoundException("subscriber not found");
                }
                try {
                        Subscriber existSub = subscriber.get();
                        Payload payload = message.getPayload();
                        payloadRepository.save(payload);
                        message.setSubscriber(existSub);
                        messageRepository.save(message);
                        subscriberRepository.save(existSub);
                        success = true;
                        log.info("Successfully saved message: " + existSub.getName() + " " + message.getPayload());
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
}