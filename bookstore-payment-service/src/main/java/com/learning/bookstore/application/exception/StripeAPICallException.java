package com.learning.bookstore.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StripeAPICallException extends RuntimeException {
    public StripeAPICallException(String message) {
        super(message);
    }
}
