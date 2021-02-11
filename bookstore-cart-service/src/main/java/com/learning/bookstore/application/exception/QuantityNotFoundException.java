package com.learning.bookstore.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class QuantityNotFoundException extends RuntimeException {
    public QuantityNotFoundException(String message) {
        super(message);
    }
}
