package com.learning.bookstore.adapter.web.v1.response;

import com.learning.bookstore.domain.CartItem;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartResponse {
    private String id;
    private String userEmail;
    private List<CartItem> cartItems;
    private double totalPrice;
}
