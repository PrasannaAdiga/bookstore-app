package com.learning.bookstore.application.port.in.product.request;


import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UpdateProductRequest extends ProductRequest {
    @NotBlank(message = "ProductId should not be blank")
    private String productId;

}
