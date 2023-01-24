package com.app.publishsubscribe.service;

import com.app.publishsubscribe.domain.Message;
import com.app.publishsubscribe.domain.Subscriber;
import com.app.publishsubscribe.repository.MessageRepository;
import com.app.publishsubscribe.repository.SubscriberRepository;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Data
@Service
public class SubscriberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriberService.class);

    private static final Integer MAX_RETRIES = 5;

    private final SubscriberRepository subscriberRepository;

    private final MessageRepository messageRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, MessageRepository messageRepository) {
        this.subscriberRepository = subscriberRepository;
        this.messageRepository = messageRepository;
    }

    public void addSubscriber(Subscriber subscriber) {
        subscriberRepository.save(subscriber);
    }

    public void removeSubscriber(Long subscriberId) {
        subscriberRepository.deleteById(subscriberId);
    }

    public void listenForMessages(Subscriber sub) {
        Message message = PublisherService.messageQueue.poll();

        while (message != null) {

            boolean success = false;
            int retries = 0;
            while (!success && retries < MAX_RETRIES) {
                try {
                    Optional<Subscriber> subscriber = subscriberRepository.findByName(sub.getName());
                    if (subscriber.isPresent()) {
                        Subscriber existSub = subscriber.get();
                        message.setSubscriber(existSub);
                        messageRepository.save(message);
                        subscriberRepository.save(existSub);
                        success = true;
                        LOGGER.info("Successfully saved message: " + existSub.getName() + " " + message.getPayload());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    retries++;
                }
            }
            if (!success) {
                System.out.println("The message could not be processed after MAX_RETRIES retries");
            }
            message = PublisherService.messageQueue.poll();
        }
    }
}