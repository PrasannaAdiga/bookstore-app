package com.bookstore.learning.infrastructure.config;

import com.bookstore.learning.application.port.in.billing.IBillingAddressQueryService;
import com.bookstore.learning.application.port.in.shipping.IShippingAddressQueryService;
import com.bookstore.learning.application.port.out.IBillingAddressDataProvider;
import com.bookstore.learning.application.port.out.IShippingAddressDataProvider;
import com.bookstore.learning.application.service.billing.BillingAddressCommandService;
import com.bookstore.learning.application.service.billing.BillingAddressQueryService;
import com.bookstore.learning.application.service.shipping.ShippingAddressCommandService;
import com.bookstore.learning.application.service.shipping.ShippingAddressQueryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddressServiceConfiguration {
    @Bean
    public BillingAddressCommandService billingAddressCommandService(IBillingAddressDataProvider billingAddressDataProvider,
                                                           IBillingAddressQueryService billingAddressQueryService) {
        return new BillingAddressCommandService(billingAddressDataProvider, billingAddressQueryService);
    }

    @Bean
    public BillingAddressQueryService billingAddressQueryService(IBillingAddressDataProvider billingAddressDataProvider) {
        return new BillingAddressQueryService(billingAddressDataProvider);
    }

    @Bean
    public ShippingAddressCommandService shippingAddressCommandService(IShippingAddressDataProvider shippingAddressDataProvider,
                                                                IShippingAddressQueryService shippingAddressQueryService) {
        return new ShippingAddressCommandService(shippingAddressDataProvider, shippingAddressQueryService);
    }

    @Bean
    public ShippingAddressQueryService shippingAddressQueryService(IShippingAddressDataProvider shippingAddressDataProvider){
        return new ShippingAddressQueryService(shippingAddressDataProvider);
    }

}
