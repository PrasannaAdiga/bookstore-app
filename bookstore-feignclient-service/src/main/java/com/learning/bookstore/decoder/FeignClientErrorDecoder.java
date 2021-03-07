package com.learning.bookstore.decoder;

import com.learning.bookstore.exception.FeignClientUnauthorizedException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class FeignClientErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder  defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 401:
                throw new FeignClientUnauthorizedException("Access token is expired or not provided while accessing " +
                        response.request().url());
            //Retry if we receive internal server error from client microservices
            case 500:
                return new RetryableException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "500 Internal Server error",
                        response.request().httpMethod(), null, null );
            default:
                return defaultErrorDecoder.decode(methodKey, response);
        }
    }

}
