package com.learning.bookstore.domain;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class CartItem {
    private String id;
    private double price;
    private double extendedPrice;

    @NotBlank(message = "ProductId should not be blank")
    private String productId;

    @Min(value = 1, message = "Quantity should be greater than 0")
    private int quantity;

    @NotNull(message = "Cart should not be null")
    private Cart cart;
}
