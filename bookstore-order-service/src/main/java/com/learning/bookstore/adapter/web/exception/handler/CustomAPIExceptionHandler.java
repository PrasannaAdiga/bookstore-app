package com.learning.bookstore.adapter.web.exception.handler;

import com.learning.bookstore.adapter.web.exception.response.RestApiResponseErrorMessage;
import com.learning.bookstore.application.exception.*;
import com.learning.bookstore.exception.response.ApiResponseErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class CustomAPIExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RestApiResponseErrorMessage> handleConstraintViolationException(final Exception exception, final HttpServletRequest request) {
        log.error("Exception handler for ConstraintViolationException: " + exception.getMessage());
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.badRequest().body(restApiResponseErrorMessage);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RestApiResponseErrorMessage> handleResourceNotFoundException(final Exception exception, final HttpServletRequest request) {
        log.error("Exception handler for ResourceNotFoundException: " + exception.getMessage());
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Resource not found!")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(restApiResponseErrorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RestApiResponseErrorMessage> handleIllegalArgumentException(final Exception exception, final HttpServletRequest request) {
        log.error("Exception handler for IllegalArgumentException: " + exception.getMessage());
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Illegal Argument Exception!")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(restApiResponseErrorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponseErrorMessage> handleUnauthorizedException(final Exception exception, final HttpServletRequest request) {
        log.error("Exception handler for UnauthorizedException: " + exception.getMessage());
        ApiResponseErrorMessage apiResponseErrorMessage = ApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponseErrorMessage);
    }

    @ExceptionHandler(EmptyShippingAddressException.class)
    public ResponseEntity<RestApiResponseErrorMessage> handleEmptyShippingAddressException(final Exception exception, final HttpServletRequest request) {
        log.error("Exception handler for EmptyShippingAddressException: " + exception.getMessage());
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("No shipping address defined for this user!")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(restApiResponseErrorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<RestApiResponseErrorMessage> handleEmptyCartException(final Exception exception, final HttpServletRequest request) {
        log.error("Exception handler for EmptyCartException: " + exception.getMessage());
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("User cart is empty with no items!")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(restApiResponseErrorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPaymentMethodException.class)
    public ResponseEntity<RestApiResponseErrorMessage> handleInvalidPaymentMethodException(final Exception exception, final HttpServletRequest request) {
        log.error("Exception handler for InvalidPaymentMethodException: " + exception.getMessage());
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid payment exception from Stripe!")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(restApiResponseErrorMessage, HttpStatus.BAD_REQUEST);
    }

}
