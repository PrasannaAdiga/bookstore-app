package com.learning.bookstore.adapter.web.v1.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemResponse {
    private String id;
    private String productId;
    private double price;
    private double extendedPrice;
    private int quantity;
}
