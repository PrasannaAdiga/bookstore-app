package com.learning.bookstore.client.fallback.handler;


import com.learning.bookstore.client.OrderFeignClient;
import com.learning.bookstore.exception.FeignClientUnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
public class OrderClientHandler implements OrderFeignClient {
    private final Throwable cause;

    private void exitIfUserIsNotAuthorized() {
        if(cause instanceof ResponseStatusException &&
                ((ResponseStatusException) cause).getStatus() == HttpStatus.UNAUTHORIZED) {
            throw new FeignClientUnauthorizedException(cause.getLocalizedMessage());
        }
    }
}
