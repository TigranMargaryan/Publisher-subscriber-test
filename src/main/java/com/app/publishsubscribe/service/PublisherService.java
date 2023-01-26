package com.app.publishsubscribe.service;

import com.app.publishsubscribe.domain.Message;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Log4j2
@Service
public class PublisherService {

    public static final BlockingQueue<Message> MESSAGE_QUEUE = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message> messageSchedule = new LinkedBlockingQueue<>();

    @Scheduled(fixedRate = 5000)
    public void scheduleMessagePublishing() {
        if (!messageSchedule.isEmpty()) {
            Message message = messageSchedule.poll();
            try {
                MESSAGE_QUEUE.put(message);
                log.info("Added message into the queue");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setMessageSchedule(Message message) {
        try {
            this.messageSchedule.put(message);
        } catch (InterruptedException e) {
            log.error("Error when trying to add message: " + e.getMessage());
        }
    }
}