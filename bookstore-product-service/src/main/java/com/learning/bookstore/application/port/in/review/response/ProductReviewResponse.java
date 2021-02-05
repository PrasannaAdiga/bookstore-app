package com.learning.bookstore.application.port.in.review.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductReviewResponse {
    private String id;
    private String productId;
    private String userId;
    private double rating;
    private String message;
}
