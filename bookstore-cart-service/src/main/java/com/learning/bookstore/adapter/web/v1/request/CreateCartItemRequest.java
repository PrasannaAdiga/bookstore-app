package com.learning.bookstore.adapter.web.v1.request;

import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
public class CreateCartItemRequest {
    @NotBlank(message = "productId should not be blank")
    private String productId;

    @Min(value = 1, message = "Quantity should be greater than 0")
    private int quantity;
}
