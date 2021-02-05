package com.learning.bookstore.adapter.persistence.impl;

import com.learning.bookstore.adapter.persistence.entity.ProductCategoryEntity;
import com.learning.bookstore.adapter.persistence.mapper.ProductCategoryEntityMapper;
import com.learning.bookstore.adapter.persistence.repository.ProductCategoryRepository;
import com.learning.bookstore.application.port.out.IProductCategoryPersistenceAdapter;
import com.learning.bookstore.infrastructure.annotation.PersistenceAdapter;
import com.learning.bookstore.domain.ProductCategory;
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
public class ProductCategoryPersistenceAdapter implements IProductCategoryPersistenceAdapter {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryEntityMapper productCategoryEntityMapper;

    @Override
    public ProductCategory createProductCategory(ProductCategory productCategory) {
        ProductCategoryEntity productCategoryEntity = productCategoryRepository.save(productCategoryEntityMapper.convertToProductCategoryEntity(productCategory));
        return productCategoryEntityMapper.convertToProductCategory(productCategoryEntity);
    }

    @Override
    public ProductCategory updateProductCategory(ProductCategory productCategory) {
        Optional<ProductCategoryEntity> existingProductCategoryEntity = productCategoryRepository.findById(productCategory.getId());
        ProductCategoryEntity productCategoryEntity = productCategoryEntityMapper.convertToProductCategoryEntity(productCategory);
        productCategoryEntity.setCreatedBy(existingProductCategoryEntity.get().getCreatedBy());
        productCategoryEntity.setCreationDate(existingProductCategoryEntity.get().getCreationDate());
        return productCategoryEntityMapper.convertToProductCategory(productCategoryRepository.save(productCategoryEntity));
    }

    @Override
    public void deleteProductCategoryById(String id) {
        productCategoryRepository.deleteById(id);
    }

    @Override
    public Optional<ProductCategory> getProductCategoryById(String id) {
        Optional<ProductCategoryEntity> productCategoryEntity = productCategoryRepository.findById(id);
        if(productCategoryEntity.isEmpty())
            return Optional.empty();
        return Optional.of(productCategoryEntityMapper.convertToProductCategory(productCategoryEntity.get()));
    }

    @Override
    public Optional<ProductCategory> getProductCategoryByName(String name) {
        Optional<ProductCategoryEntity> productCategoryEntity = productCategoryRepository.findByName(name);
        if(productCategoryEntity.isEmpty())
            return Optional.empty();
        return Optional.of(productCategoryEntityMapper.convertToProductCategory(productCategoryEntity.get()));
    }

    @Override
    public List<ProductCategory> getAllProductCategory(Integer page, Integer size, String sort) {
        Page<ProductCategoryEntity> productCategoryEntities =  productCategoryRepository.findAll(PageRequest.of(page, size, Sort.by(sort).ascending()));
        if(productCategoryEntities.hasContent()) {
            return productCategoryEntities.map(new Function<ProductCategoryEntity, ProductCategory>() {
                @Override
                public ProductCategory apply(ProductCategoryEntity productCategoryEntity) {
                    return productCategoryEntityMapper.convertToProductCategory(productCategoryEntity);
                }
            }).getContent();
        } else {
            return new ArrayList<ProductCategory>();
        }
    }

    @Override
    public long getTotalProductCategory() {
        return productCategoryRepository.count();
    }
}
