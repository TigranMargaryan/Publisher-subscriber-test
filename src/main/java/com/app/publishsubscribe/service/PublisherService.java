package com.app.publishsubscribe.service;

import com.app.publishsubscribe.domain.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class PublisherService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherService.class);

    public static BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message> messageSchedule = new LinkedBlockingQueue<>();

    @Scheduled(fixedRate = 5000)
    public void scheduleMessagePublishing() {
        if (!messageSchedule.isEmpty()) {
            Message message = messageSchedule.poll();
            try {
                messageQueue.put(message);
                LOGGER.info("Added message into the queue");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setMessageSchedule(Message message) {
        try {
            this.messageSchedule.put(message);
        } catch (InterruptedException e) {
            LOGGER.error("Error when trying to add message: " + e.getMessage());
        }
    }
}