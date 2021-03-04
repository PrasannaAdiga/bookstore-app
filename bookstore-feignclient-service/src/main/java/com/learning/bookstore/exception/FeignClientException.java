package com.learning.bookstore.exception;

public class FeignClientException extends RuntimeException {
    public FeignClientException(String message) {
        super(message);
    }
}
