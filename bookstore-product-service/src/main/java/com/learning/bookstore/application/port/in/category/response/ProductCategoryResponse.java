package com.learning.bookstore.application.port.in.category.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductCategoryResponse {
    private String id;
    private String name;
    private String description;
}
