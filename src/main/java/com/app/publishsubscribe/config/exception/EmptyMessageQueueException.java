package com.app.publishsubscribe.config.exception;

public class EmptyMessageQueueException extends RuntimeException {

    public EmptyMessageQueueException(String message) {
        super(message);
    }
}
