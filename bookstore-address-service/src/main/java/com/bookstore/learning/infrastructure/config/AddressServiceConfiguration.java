package com.bookstore.learning.infrastructure.config;

import com.bookstore.learning.application.port.in.billing.IBillingAddressQueryService;
import com.bookstore.learning.application.port.in.shipping.IShippingAddressQueryService;
import com.bookstore.learning.application.port.out.IBillingAddressDataProvider;
import com.bookstore.learning.application.port.out.IShippingAddressDataProvider;
import com.bookstore.learning.application.service.billing.BillingAddressCommandService;
import com.bookstore.learning.application.service.billing.BillingAddressQueryService;
import com.bookstore.learning.application.service.shipping.ShippingAddressCommandService;
import com.bookstore.learning.application.service.shipping.ShippingAddressQueryService;
import com.bookstore.learning.infrastructure.util.PrincipalResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddressServiceConfiguration {
    @Bean
    public BillingAddressCommandService billingAddressCommandService(IBillingAddressDataProvider billingAddressDataProvider,
                                                           IBillingAddressQueryService billingAddressQueryService,
                                                                     PrincipalResolver principalResolver) {
        return new BillingAddressCommandService(billingAddressDataProvider, billingAddressQueryService, principalResolver);
    }

    @Bean
    public BillingAddressQueryService billingAddressQueryService(IBillingAddressDataProvider billingAddressDataProvider,
                                                                 PrincipalResolver principalResolver) {
        return new BillingAddressQueryService(billingAddressDataProvider, principalResolver);
    }

    @Bean
    public ShippingAddressCommandService shippingAddressCommandService(IShippingAddressDataProvider shippingAddressDataProvider,
                                                                IShippingAddressQueryService shippingAddressQueryService,
                                                                       PrincipalResolver principalResolver) {
        return new ShippingAddressCommandService(shippingAddressDataProvider, shippingAddressQueryService, principalResolver);
    }

    @Bean
    public ShippingAddressQueryService shippingAddressQueryService(IShippingAddressDataProvider shippingAddressDataProvider,
                                                                   PrincipalResolver principalResolver){
        return new ShippingAddressQueryService(shippingAddressDataProvider, principalResolver);
    }

}
