package com.learning.bookstore.application.port.in.product.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private double price;
    private String imageId;
    private String categoryId;
    private int availableCount;
    private Double averageRating;
    private int noOfRatings;
}
