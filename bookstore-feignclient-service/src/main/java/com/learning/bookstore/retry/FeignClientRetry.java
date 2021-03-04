package com.learning.bookstore.retry;

import feign.Retryer;
import org.springframework.stereotype.Component;

@Component
public class FeignClientRetry extends Retryer.Default {
    public FeignClientRetry() {
        super();
    }
}
