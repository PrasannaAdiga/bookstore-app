package com.learning.bookstore.application.service.item.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
