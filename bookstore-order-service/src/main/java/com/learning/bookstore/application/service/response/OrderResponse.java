package com.learning.bookstore.application.service.response;

import com.learning.bookstore.web.BillingAddressResponse;
import com.learning.bookstore.web.ShippingAddressResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String orderId;
    private List<OrderItemResponse> orderItems;
    private ShippingAddressResponse shippingAddress;
    private BillingAddressResponse billingAddress;
    private CardResponse card;
    private Double itemsTotalPrice;
    private Double taxPrice;
    private Double shippingPrice;
    private Double totalPrice;
    private boolean isPaid;
    private LocalDateTime paymentDate;
    private boolean isDelivered;
    private String paymentReceiptUrl;
    private LocalDateTime deliveredDate;
    private Date created_at;
}
