package com.learning.bookstore.application.port.out;

import com.learning.bookstore.domain.Product;

import java.util.List;
import java.util.Optional;

public interface IProductPersistenceAdapter {
    Product createProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(String id);
    Optional<Product> getProductById(String id);
    Optional<Product> getProductByName(String name);
    List<Product> getAllProduct(Integer page, Integer size, String sort);
    long getTotalProduct();
}
