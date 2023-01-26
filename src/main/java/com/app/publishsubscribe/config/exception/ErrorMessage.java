package com.app.publishsubscribe.config.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ErrorMessage {

    private int statusCode;
    private LocalDate timestamp;
    private String message;
    private String description;

    public ErrorMessage(int statusCode, LocalDate timestamp, String message, String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }
}
