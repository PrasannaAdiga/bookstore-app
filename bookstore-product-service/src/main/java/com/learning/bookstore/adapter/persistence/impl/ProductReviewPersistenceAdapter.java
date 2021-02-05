package com.learning.bookstore.adapter.persistence.impl;

import com.learning.bookstore.adapter.persistence.entity.ProductReviewEntity;
import com.learning.bookstore.adapter.persistence.mapper.ProductReviewEntityMapper;
import com.learning.bookstore.adapter.persistence.repository.ProductReviewRepository;
import com.learning.bookstore.application.port.out.IProductReviewPersistenceAdapter;
import com.learning.bookstore.infrastructure.annotation.PersistenceAdapter;
import com.learning.bookstore.domain.ProductReview;
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
public class ProductReviewPersistenceAdapter implements IProductReviewPersistenceAdapter {
    private final ProductReviewRepository productReviewRepository;
    private final ProductReviewEntityMapper productReviewEntityMapper;

    @Override
    public ProductReview createOrUpdateProductReview(ProductReview productReview) {
        Optional<ProductReviewEntity> existingProductReviewEntity = productReview.getId() != null ? productReviewRepository.findById(productReview.getId()) : Optional.empty();
        ProductReviewEntity productReviewEntity = productReviewEntityMapper.convertToProductReviewEntity(productReview);
        existingProductReviewEntity.ifPresent((eProductReview) -> {
            productReviewEntity.setCreatedBy(eProductReview.getCreatedBy());
            productReviewEntity.setCreationDate(eProductReview.getCreationDate());
        });
        return productReviewEntityMapper.convertToProductReview(productReviewRepository.save(productReviewEntity));
    }

    @Override
    public List<ProductReview> getAllReviewOfProductByPage(String productId, Integer page, Integer size, String sort) {
        Page<ProductReviewEntity> productReviewEntities =  productReviewRepository.getAllReviewOfProductByPage(productId, PageRequest.of(page, size, Sort.by(sort).ascending()));
        if(productReviewEntities.hasContent()) {
            return productReviewEntities.map(new Function<ProductReviewEntity, ProductReview>() {
                @Override
                public ProductReview apply(ProductReviewEntity productReviewEntity) {
                    return productReviewEntityMapper.convertToProductReview(productReviewEntity);
                }
            }).getContent();
        } else {
            return new ArrayList<ProductReview>();
        }
    }

    @Override
    public List<Integer> getAllReviewRatingOfProduct(String productId) {
        return productReviewRepository.getAllReviewRatingOfProduct(productId);
    }

}
