package com.learning.bookstore.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class ResourceFoundException extends RuntimeException {
    public ResourceFoundException(String message) {
        super(message);
    }
}
