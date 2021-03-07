package com.learning.bookstore.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class EmptyShippingAddressException extends RuntimeException {
    public EmptyShippingAddressException(String message) {
        super(message);
    }
}
