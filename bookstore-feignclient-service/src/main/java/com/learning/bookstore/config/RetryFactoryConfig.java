package com.learning.bookstore.config;

import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRetryProperties;
import org.springframework.cloud.loadbalancer.blocking.retry.BlockingLoadBalancedRetryFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RetryFactoryConfig {
    /**
     * This Bean definition with @Primary annotation was required to fix one of the bug
     * When we have spring-cloud-openfeign and spring-cloud-sleuth-zipkin plugins in the classpath,
     * While running the application, it throws multiple bean definition for the bean LoadBalancedRetryFactory.
     * So in order to fix that we define a bean of type LoadBalancedRetryFactory and make it as Primary bean
     */
    @Bean
    @Primary
    @Order(1000)
    LoadBalancedRetryFactory loadBalancedRetryFactory(
            LoadBalancerRetryProperties retryProperties) {
        return new BlockingLoadBalancedRetryFactory(retryProperties);
    }

}
