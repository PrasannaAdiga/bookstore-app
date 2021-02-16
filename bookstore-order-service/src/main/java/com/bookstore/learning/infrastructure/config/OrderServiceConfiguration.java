package com.bookstore.learning.infrastructure.config;

import com.bookstore.learning.adapter.client.AddressFeignClient;
import com.bookstore.learning.adapter.client.CartFeignClient;
import com.bookstore.learning.adapter.client.PaymentFeignClient;
import com.bookstore.learning.application.port.out.IOrderDataProvider;
import com.bookstore.learning.application.service.OrderCommandService;
import com.bookstore.learning.application.service.OrderQueryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderServiceConfiguration {
    @Bean
    public OrderCommandService orderCommandService(IOrderDataProvider orderDataProvider,
                                                   AddressFeignClient addressFeignClient,
                                                   CartFeignClient cartFeignClient,
                                                   PaymentFeignClient paymentFeignClient) {
        return new OrderCommandService(orderDataProvider, addressFeignClient, cartFeignClient, paymentFeignClient);
    }

    @Bean
    public OrderQueryService orderQueryService(IOrderDataProvider orderDataProvider,
                                               AddressFeignClient addressFeignClient,
                                               PaymentFeignClient paymentFeignClient) {
        return new OrderQueryService(orderDataProvider, addressFeignClient, paymentFeignClient);
    }

}
