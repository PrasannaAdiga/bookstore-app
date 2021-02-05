package com.learning.bookstore.application.port.in.product;

import com.learning.bookstore.application.port.in.product.request.CreateProductRequest;
import com.learning.bookstore.application.port.in.product.request.UpdateProductRequest;

public interface IProductCommandService {
    String createProduct(CreateProductRequest createProductRequest);
    void updateProduct(UpdateProductRequest updateProductRequest);
    void deleteProductById(String id);
}
