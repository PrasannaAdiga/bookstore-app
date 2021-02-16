package com.bookstore.learning.domain;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String id;

    @NotNull(message = "Order should not be null")
    private Order order;

    @NotBlank(message = "Product Id should not be blank")
    private String productId;

    @Positive(message = "Quantity should be positive")
    private int quantity;

    @Positive(message = "Order Item Price should be positive")
    private double orderItemPrice;

    @Positive(message = "Order extended price should be positive")
    private double orderExtendedPrice;

}
