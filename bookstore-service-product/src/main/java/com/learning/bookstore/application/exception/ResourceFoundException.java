package com.learning.bookstore.application.exception;

//@ResponseStatus(HttpStatus.FOUND)
public class ResourceFoundException extends RuntimeException {
    public ResourceFoundException(String message) {
        super(message);
    }
}
