package com.learning.bookstore.application.port.in.review.request;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@Getter
public class CreateOrUpdateReviewRequest {
    private String id;

    @NotBlank(message = "ProductId should not be blank")
    private String productId;

    @NotBlank(message = "UserId should not be blank")
    private String userId;

    @Range(min = 1, max = 5, message = "product rating must be between 1 to 5")
    private int rating;

    @Length(min = 5, max = 250, message = "Review message should be 5 to 250 characters")
    private String message;
}
