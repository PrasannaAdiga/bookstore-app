package com.learning.bookstore.application.port.in.category.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UpdateProductCategoryRequest extends ProductCategoryRequest {
    @NotBlank(message = "ProductCategoryId should not be blank")
    private String productCategoryId;

}
