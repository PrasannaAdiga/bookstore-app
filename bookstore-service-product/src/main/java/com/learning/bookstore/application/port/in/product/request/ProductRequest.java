package com.learning.bookstore.application.port.in.product.request;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
public abstract class ProductRequest {
    @NotBlank(message = "Product name should not be blank")
    private String productName;

    @Length(min = 5, max = 100, message = "Product description should be 5 to 100 characters")
    private String productDescription;

    @Positive(message = "Product price should be positive")
    private double productPrice;

    private String productImageId;

    @PositiveOrZero(message = "Available product count should be positive")
    private int availableProductCount;

    @NotBlank(message = "ProductCategoryId should not be blank")
    private String productCategoryId;
}
