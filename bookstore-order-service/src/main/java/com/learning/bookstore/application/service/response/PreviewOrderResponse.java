package com.learning.bookstore.application.service.response;

import com.learning.bookstore.web.BillingAddressResponse;
import com.learning.bookstore.web.ShippingAddressResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreviewOrderResponse {
    @Builder.Default
    private List<OrderItemResponse> orderItems = new ArrayList<>();
    private ShippingAddressResponse shippingAddress;
    private BillingAddressResponse billingAddress;
    private CardResponse card;
    private Double itemsTotalPrice;
    private Double taxPrice;
    private Double shippingPrice;
    private Double totalPrice;
}
