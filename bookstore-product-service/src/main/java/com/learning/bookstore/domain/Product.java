package com.learning.bookstore.domain;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter
@Builder
public class Product {
    private String id;

    @NotBlank(message = "Product name should not be blank")
    private String name;

    @Length(min = 5, max = 100, message = "Product description should be 5 to 100 characters")
    private String description;

    @Positive(message = "Product price should be positive")
    private double price;

    private String imageId;

    @PositiveOrZero(message = "Available product count should be positive")
    private int availableCount;

    @NotNull(message = "Product category should be selected")
    private ProductCategory category;

}
