package com.bookstore.learning.application.service.response;

import com.bookstore.learning.adapter.client.dto.BillingAddressResponse;
import com.bookstore.learning.adapter.client.dto.ShippingAddressResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreviewOrderResponse {
    private List<OrderItemResponse> orderItems;
    private ShippingAddressResponse shippingAddress;
    private BillingAddressResponse billingAddress;
    private CardResponse card;
    private Double itemsTotalPrice;
    private Double taxPrice;
    private Double shippingPrice;
    private Double totalPrice;
}
