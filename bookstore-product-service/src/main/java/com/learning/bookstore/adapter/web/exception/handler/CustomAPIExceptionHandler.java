package com.learning.bookstore.adapter.web.exception.handler;

import com.learning.bookstore.adapter.web.exception.response.RestApiResponseErrorMessage;
import com.learning.bookstore.application.exception.ResourceFoundException;
import com.learning.bookstore.application.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomAPIExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RestApiResponseErrorMessage> handleConstraintViolationException(final Exception exception, final HttpServletRequest request) {
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.badRequest().body(restApiResponseErrorMessage);
    }

    @ExceptionHandler(ResourceFoundException.class)
    public ResponseEntity<RestApiResponseErrorMessage> handleResourceFoundException(final Exception exception, final HttpServletRequest request) {
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.FOUND.value())
                .error("Resource already exists!")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(restApiResponseErrorMessage, HttpStatus.FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RestApiResponseErrorMessage> handleResourceNotFoundException(final Exception exception, final HttpServletRequest request) {
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
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Illegal Argument Exception!")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(restApiResponseErrorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<RestApiResponseErrorMessage> handleNoSuchFileException(final Exception exception, final HttpServletRequest request) {
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("No such image file found!")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(restApiResponseErrorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
