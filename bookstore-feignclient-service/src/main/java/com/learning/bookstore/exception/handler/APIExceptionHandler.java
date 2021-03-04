package com.learning.bookstore.exception.handler;

import com.learning.bookstore.exception.FeignClientException;
import com.learning.bookstore.exception.response.ApiResponseErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class APIExceptionHandler {
    @ExceptionHandler(FeignClientException.class)
    public ResponseEntity<ApiResponseErrorMessage> handleConstraintViolationException(final Exception exception, final HttpServletRequest request) {
        ApiResponseErrorMessage apiResponseErrorMessage = ApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.REQUEST_TIMEOUT.getReasonPhrase())
                .message(exception.getMessage() + " while communicating with external microservice")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseErrorMessage);
    }

}
