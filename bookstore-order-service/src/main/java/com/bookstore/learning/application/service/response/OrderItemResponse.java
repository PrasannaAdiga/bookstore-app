package com.bookstore.learning.application.service.response;

import com.bookstore.learning.domain.Order;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class OrderItemResponse {
    private String id;
    private Order order;
    private String productId;
    private int quantity;
    private double orderItemPrice;
    private double orderExtendedPrice;

}
