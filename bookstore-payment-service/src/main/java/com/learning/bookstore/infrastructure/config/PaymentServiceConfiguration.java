package com.learning.bookstore.infrastructure.config;

import com.learning.bookstore.application.port.out.IUserPaymentCustomerDataProvider;
import com.learning.bookstore.application.service.PaymentCommandService;
import com.learning.bookstore.application.service.PaymentMethodCommandService;
import com.learning.bookstore.application.service.PaymentMethodQueryService;
import com.learning.bookstore.infrastructure.util.PrincipalResolver;
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
