package com.learning.bookstore.application.exception;

public class EmptyShippingAddressException extends RuntimeException {
    public EmptyShippingAddressException(String message) {
        super(message);
    }
}
