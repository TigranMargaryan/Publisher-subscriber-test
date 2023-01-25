package com.app.publishsubscribe.service;

import com.app.publishsubscribe.domain.Message;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class PublisherService {

    public static BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message> messageSchedule = new LinkedBlockingQueue<>();

    @Scheduled(fixedRate = 5000)
    public void scheduleMessagePublishing() {
        if (!messageSchedule.isEmpty()) {
            Message message = messageSchedule.poll();
            try {
                messageQueue.put(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setMessageSchedule(Message message) {
        try {
            this.messageSchedule.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}