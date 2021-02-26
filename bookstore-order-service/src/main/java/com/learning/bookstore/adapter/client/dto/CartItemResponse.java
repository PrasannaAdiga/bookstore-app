package com.learning.bookstore.adapter.client.dto;

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
