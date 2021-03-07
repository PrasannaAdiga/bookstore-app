package com.learning.bookstore.exception;

public class FeignClientUnauthorizedException extends RuntimeException {
    public FeignClientUnauthorizedException(String message) {
        super(message);
    }
}
