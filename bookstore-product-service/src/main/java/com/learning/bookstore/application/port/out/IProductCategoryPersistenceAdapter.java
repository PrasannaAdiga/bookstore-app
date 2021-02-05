package com.learning.bookstore.application.port.out;

import com.learning.bookstore.domain.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface IProductCategoryPersistenceAdapter {
    ProductCategory createProductCategory(ProductCategory productCategory);
    ProductCategory updateProductCategory(ProductCategory productCategory);
    void deleteProductCategoryById(String id);
    Optional<ProductCategory> getProductCategoryById(String id);
    Optional<ProductCategory> getProductCategoryByName(String name);
    List<ProductCategory> getAllProductCategory(Integer page, Integer size, String sort);
    long getTotalProductCategory();
}
