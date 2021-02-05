package com.learning.bookstore.domain;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class ProductCategory {
    private String id;

    @NotBlank(message = "Product category name should not be blank")
    private String name;

    @Length(min = 5, max = 100, message = "Product category description should be 5 to 100 characters")
    private String description;
}
