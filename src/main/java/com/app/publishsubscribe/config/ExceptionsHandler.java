package com.app.publishsubscribe.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionsHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionsHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Throwable throwable) {
        List<String> result = new ArrayList<>();
        while (throwable != null) {
            result.add(throwable.getMessage());
            throwable = throwable.getCause();
        }
        LOG.error("Configuration failed: " + result);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
