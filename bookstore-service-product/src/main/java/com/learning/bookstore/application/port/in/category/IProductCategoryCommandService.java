package com.learning.bookstore.application.port.in.category;

import com.learning.bookstore.application.port.in.category.request.CreateProductCategoryRequest;
import com.learning.bookstore.application.port.in.category.request.UpdateProductCategoryRequest;

public interface IProductCategoryCommandService {
    String createProductCategory(CreateProductCategoryRequest productCategoryRequest);
    void updateProductCategory(UpdateProductCategoryRequest updateProductCategoryRequest);
    void deleteProductCategoryById(String id);
}
