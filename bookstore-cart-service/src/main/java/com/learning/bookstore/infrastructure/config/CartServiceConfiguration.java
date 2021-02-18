package com.learning.bookstore.infrastructure.config;

import com.learning.bookstore.adapter.client.ProductFeignClient;
import com.learning.bookstore.application.port.in.cart.ICartQueryService;
import com.learning.bookstore.application.port.out.ICartDataProvider;
import com.learning.bookstore.application.port.out.ICartItemDataProvider;
import com.learning.bookstore.application.service.cart.CartCommandService;
import com.learning.bookstore.application.service.cart.CartQueryService;
import com.learning.bookstore.application.service.item.CartItemCommandService;
import com.learning.bookstore.application.service.item.CartItemQueryService;
import com.learning.bookstore.infrastructure.util.PrincipalResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CartServiceConfiguration {
    @Bean
    public CartCommandService cartCommandService(ICartDataProvider cartDataProvider,
                                                 PrincipalResolver principalResolver) {
        return new CartCommandService(cartDataProvider, principalResolver);
    }

    @Bean
    public CartQueryService cartQueryService(ICartDataProvider cartDataProvider,
                                             PrincipalResolver principalResolver) {
        return new CartQueryService(cartDataProvider, principalResolver);
    }

    @Bean
    public CartItemCommandService cartItemCommandService(ICartQueryService cartQueryService,
                                                         ICartItemDataProvider cartItemDataProvider,
                                                         ProductFeignClient productFeignClient,
                                                         PrincipalResolver principalResolver) {
        return new CartItemCommandService(cartQueryService, cartItemDataProvider,
                productFeignClient, principalResolver);
    }

    @Bean
    public CartItemQueryService cartItemQueryService(ICartItemDataProvider cartItemDataProvider) {
        return new CartItemQueryService(cartItemDataProvider);
    }

}
