package com.learning.bookstore.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Cart {
    private String id;
    private String userEmail;
    private List<CartItem> cartItems;
    private double totalPrice;
}
