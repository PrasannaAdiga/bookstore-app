package com.learning.bookstore.application.port.in.category;

import com.learning.bookstore.application.port.in.category.response.ProductCategoryResponse;

import java.util.List;

public interface IProductCategoryQueryService {
    ProductCategoryResponse getProductCategoryById(String id);
    List<ProductCategoryResponse> getAllProductCategory(Integer page, Integer size, String sort);
    long getTotalProductCategory();
}
