package com.learning.bookstore.adapter.persistence.impl;

import com.learning.bookstore.adapter.persistence.entity.ProductEntity;
import com.learning.bookstore.adapter.persistence.mapper.ProductEntityMapper;
import com.learning.bookstore.adapter.persistence.repository.ProductRepository;
import com.learning.bookstore.application.port.out.IProductPersistenceAdapter;
import com.learning.bookstore.infrastructure.annotation.PersistenceAdapter;
import com.learning.bookstore.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@PersistenceAdapter
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements IProductPersistenceAdapter {
    private final ProductRepository productRepository;
    private final ProductEntityMapper productEntityMapper;

    @Override
    public Product createProduct(Product product) {
        ProductEntity productEntity = productRepository.save(productEntityMapper.convertToProductEntity(product));
        return productEntityMapper.convertToProduct(productEntity);
    }

    @Override
    public Product updateProduct(Product product) {
        Optional<ProductEntity> existingProductEntity = productRepository.findById(product.getId());
        ProductEntity productEntity = productEntityMapper.convertToProductEntity(product);
        productEntity.setCreatedBy(existingProductEntity.get().getCreatedBy());
        productEntity.setCreationDate(existingProductEntity.get().getCreationDate());
        return productEntityMapper.convertToProduct(productRepository.save(productEntity));
    }

    @Override
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public Optional<Product> getProductById(String id) {
        Optional<ProductEntity> productEntity = productRepository.findById(id);
        if(productEntity.isEmpty())
            return Optional.empty();
        return Optional.of(productEntityMapper.convertToProduct(productEntity.get()));
    }

    @Override
    public Optional<Product> getProductByName(String name) {
        Optional<ProductEntity> productEntity = productRepository.findByName(name);
        if(productEntity.isEmpty())
            return Optional.empty();
        return Optional.of(productEntityMapper.convertToProduct(productEntity.get()));
    }

    @Override
    public List<Product> getAllProduct(Integer page, Integer size, String sort) {
        Page<ProductEntity> productEntities =  productRepository.findAll(PageRequest.of(page, size, Sort.by(sort).ascending()));
        if(productEntities.hasContent()) {
            return productEntities.map(new Function<ProductEntity, Product>() {
                @Override
                public Product apply(ProductEntity productEntity) {
                    return productEntityMapper.convertToProduct(productEntity);
                }
            }).getContent();
        } else {
            return new ArrayList<Product>();
        }
    }

    @Override
    public long getTotalProduct() {
        return productRepository.count();
    }
}
