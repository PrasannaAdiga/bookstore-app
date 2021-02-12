package com.bookstore.learning.infrastructure.config;

import com.bookstore.learning.application.port.out.IUserPaymentCustomerDataProvider;
import com.bookstore.learning.application.service.PaymentCommandService;
import com.bookstore.learning.application.service.PaymentMethodCommandService;
import com.bookstore.learning.application.service.PaymentMethodQueryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentServiceConfiguration {
    @Bean
    public PaymentCommandService paymentCommandService(IUserPaymentCustomerDataProvider userPaymentCustomerDataProvider) {
        return new PaymentCommandService(userPaymentCustomerDataProvider);
    }

    @Bean
    public PaymentMethodCommandService paymentMethodCommandService(IUserPaymentCustomerDataProvider userPaymentCustomerDataProvider) {
        return new PaymentMethodCommandService(userPaymentCustomerDataProvider);
    }

    @Bean
    public PaymentMethodQueryService paymentMethodQueryService(IUserPaymentCustomerDataProvider userPaymentCustomerDataProvider) {
        return new PaymentMethodQueryService(userPaymentCustomerDataProvider);
    }

}
