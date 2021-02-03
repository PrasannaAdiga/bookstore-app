package com.learning.bookstore.application.port.in.product;

import com.learning.bookstore.application.port.in.product.response.ProductResponse;

import java.util.List;

public interface IProductQueryService {
    ProductResponse getProductById(String id);
    List<ProductResponse> getAllProduct(Integer page, Integer size, String sort);
    long getTotalProduct();
}
