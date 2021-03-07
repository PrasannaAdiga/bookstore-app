package com.learning.bookstore.application.service.response;

import com.learning.bookstore.web.BillingAddressResponse;
import com.learning.bookstore.web.ShippingAddressResponse;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String orderId;
    @Builder.Default
    @JsonIgnore
    private List<OrderItemResponse> orderItems = new ArrayList<>();
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
