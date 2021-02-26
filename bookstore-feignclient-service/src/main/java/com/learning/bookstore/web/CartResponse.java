package com.learning.bookstore.web;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartResponse {
    private String id;
    private String userEmail;
    private List<CartItemResponse> cartItems;
    private double totalPrice;
}
