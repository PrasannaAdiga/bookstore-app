package com.learning.bookstore.domain;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class ProductReview {
    private String id;

    @NotBlank(message = "Product should be selected")
    private Product product;

    @NotBlank(message = "UserId should not be blank")
    private String userId;

    @Range(min = 1, max = 5, message = "product rating must be between 1 to 5")
    private int rating;

    @Length(min = 5, max = 250, message = "Review message should be 5 to 250 characters")
    private String message;

}
