package com.learning.bookstore.decoder;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class FeignClientErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        Exception exception = defaultErrorDecoder.decode(s, response);
        if(exception instanceof RetryableException){
            return exception;
        }
        //Retry if we receive internal server error from client microservices
        if(response.status() == HttpStatus.INTERNAL_SERVER_ERROR.value()){
            return new RetryableException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "500 Internal Server error",
                    response.request().httpMethod(), null, null );
        }
        return exception;
    }
}
