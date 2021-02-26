package com.learning.bookstore.infrastructure.config;

import com.learning.bookstore.adapter.client.AddressFeignClient;
import com.learning.bookstore.adapter.client.CartFeignClient;
import com.learning.bookstore.adapter.client.PaymentFeignClient;
import com.learning.bookstore.application.port.out.IOrderDataProvider;
import com.learning.bookstore.application.service.OrderCommandService;
import com.learning.bookstore.application.service.OrderQueryService;
import com.learning.bookstore.infrastructure.util.PrincipalResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderServiceConfiguration {
    @Bean
    public OrderCommandService orderCommandService(IOrderDataProvider orderDataProvider,
                                                   AddressFeignClient addressFeignClient,
                                                   CartFeignClient cartFeignClient,
                                                   PaymentFeignClient paymentFeignClient,
                                                   PrincipalResolver principalResolver) {
        return new OrderCommandService(orderDataProvider, addressFeignClient, cartFeignClient,
                paymentFeignClient, principalResolver);
    }

    @Bean
    public OrderQueryService orderQueryService(IOrderDataProvider orderDataProvider,
                                               AddressFeignClient addressFeignClient,
                                               PaymentFeignClient paymentFeignClient,
                                               PrincipalResolver principalResolver) {
        return new OrderQueryService(orderDataProvider, addressFeignClient,
                paymentFeignClient, principalResolver);
    }

}
