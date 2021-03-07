package com.learning.bookstore.exception.handler;

import com.learning.bookstore.exception.FeignClientUnauthorizedException;
import com.learning.bookstore.exception.response.ApiResponseErrorMessage;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class FeignClientExceptionHandler {
    @ExceptionHandler(UnknownHostException.class)
    public ResponseEntity<ApiResponseErrorMessage> handleUnknownHostException(final Exception exception, final HttpServletRequest request) {
        ApiResponseErrorMessage apiResponseErrorMessage = ApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.REQUEST_TIMEOUT.value())
                .error(HttpStatus.REQUEST_TIMEOUT.getReasonPhrase())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(apiResponseErrorMessage);
    }

    @ExceptionHandler(FeignClientUnauthorizedException.class)
    public ResponseEntity<ApiResponseErrorMessage> handleUnauthorizedException(final Exception exception, final HttpServletRequest request) {
        ApiResponseErrorMessage apiResponseErrorMessage = ApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponseErrorMessage);
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<ApiResponseErrorMessage> handleFeignExceptionNotFound(final Exception exception, final HttpServletRequest request) {
        ApiResponseErrorMessage apiResponseErrorMessage = ApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponseErrorMessage);
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    public ResponseEntity<ApiResponseErrorMessage> handleFeignExceptionBadRequest(final Exception exception, final HttpServletRequest request) {
        ApiResponseErrorMessage apiResponseErrorMessage = ApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponseErrorMessage);
    }

}

