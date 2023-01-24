package com.app.publishsubscribe.service;

import com.app.publishsubscribe.domain.Message;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class PublisherService {

    public static BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();

    @Scheduled(fixedRate = 5000)
    public void scheduleMessagePublishing() {
        Message message = generateMessage();
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Message generateMessage() {
        Message message = new Message();
        message.setPayload("Random message: " + UUID.randomUUID());
        return message;
    }
}