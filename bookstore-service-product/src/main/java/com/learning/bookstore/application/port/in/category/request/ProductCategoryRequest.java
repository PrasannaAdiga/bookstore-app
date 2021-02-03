package com.learning.bookstore.application.port.in.category.request;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
public abstract class ProductCategoryRequest {
    @NotBlank(message = "ProductCategoryName should not be blank")
    private String productCategoryName;

    @Length(min = 5, max = 100, message = "ProductCategoryDescription should be 5 to 100 characters")
    private String productCategoryDescription;
}
