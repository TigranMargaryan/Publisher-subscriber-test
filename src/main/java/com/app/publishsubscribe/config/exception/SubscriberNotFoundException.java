package com.app.publishsubscribe.config.exception;

public class SubscriberNotFoundException extends RuntimeException {

    public SubscriberNotFoundException(String message) {
        super(message);
    }
}
