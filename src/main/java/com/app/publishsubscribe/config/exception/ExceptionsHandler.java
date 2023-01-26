package com.app.publishsubscribe.config.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDate;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {

        return new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDate.now(),
                ex.getMessage(),
                request.getDescription(false));
    }
}

