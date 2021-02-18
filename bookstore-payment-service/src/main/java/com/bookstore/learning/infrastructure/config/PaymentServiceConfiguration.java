package com.bookstore.learning.infrastructure.config;

import com.bookstore.learning.application.port.out.IUserPaymentCustomerDataProvider;
import com.bookstore.learning.application.service.PaymentCommandService;
import com.bookstore.learning.application.service.PaymentMethodCommandService;
import com.bookstore.learning.application.service.PaymentMethodQueryService;
import com.bookstore.learning.infrastructure.util.PrincipalResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentServiceConfiguration {
    @Bean
    public PaymentCommandService paymentCommandService(IUserPaymentCustomerDataProvider userPaymentCustomerDataProvider,
                                                       PrincipalResolver principalResolver) {
        return new PaymentCommandService(userPaymentCustomerDataProvider, principalResolver);
    }

    @Bean
    public PaymentMethodCommandService paymentMethodCommandService(IUserPaymentCustomerDataProvider userPaymentCustomerDataProvider,
                                                                   PrincipalResolver principalResolver) {
        return new PaymentMethodCommandService(userPaymentCustomerDataProvider, principalResolver);
    }

    @Bean
    public PaymentMethodQueryService paymentMethodQueryService(IUserPaymentCustomerDataProvider userPaymentCustomerDataProvider,
                                                               PrincipalResolver principalResolver) {
        return new PaymentMethodQueryService(userPaymentCustomerDataProvider, principalResolver);
    }

}
