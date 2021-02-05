package com.learning.bookstore.application.service.review;

import com.learning.bookstore.domain.Product;
import com.learning.bookstore.domain.ProductReview;
import com.learning.bookstore.application.exception.ResourceNotFoundException;
import com.learning.bookstore.application.port.in.review.IProductReviewCommandService;
import com.learning.bookstore.application.port.in.review.request.CreateOrUpdateReviewRequest;
import com.learning.bookstore.application.port.out.IProductPersistenceAdapter;
import com.learning.bookstore.application.port.out.IProductReviewPersistenceAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class ProductReviewCommandService implements IProductReviewCommandService {
    private final IProductReviewPersistenceAdapter productReviewPersistenceAdapter;
    private final IProductPersistenceAdapter productPersistenceAdapter;

    @Override
    public String createOrUpdateProductReview(CreateOrUpdateReviewRequest createOrUpdateReviewRequest) {
        productPersistenceAdapter.getProductById(createOrUpdateReviewRequest.getProductId()).orElseThrow(() -> {
            log.error("ResourceNotFoundException in ProductReviewCommandService.createOrUpdateProductReview: Product with id {} not found", createOrUpdateReviewRequest.getProductId());
            throw new ResourceNotFoundException("Product with id " + createOrUpdateReviewRequest.getProductId() + " not found!");
        });
        ProductReview productReview = buildProductReview(createOrUpdateReviewRequest);
        return productReviewPersistenceAdapter.createOrUpdateProductReview(productReview).getId();
    }

    private ProductReview buildProductReview(CreateOrUpdateReviewRequest createOrUpdateReviewRequest) {
        return ProductReview.builder()
                .id(createOrUpdateReviewRequest.getId())
                .product(getProduct(createOrUpdateReviewRequest))
                .userId(createOrUpdateReviewRequest.getUserId())
                .rating(createOrUpdateReviewRequest.getRating())
                .message(createOrUpdateReviewRequest.getMessage())
                .build();
    }

    private Product getProduct(CreateOrUpdateReviewRequest createOrUpdateReviewRequest) {
        Optional<Product> product = productPersistenceAdapter.getProductById(createOrUpdateReviewRequest.getProductId());
        product.orElseThrow(() -> {
            log.error("ResourceNotFoundException in ProductReviewCommandService.getProduct: Product with Id {} not found", createOrUpdateReviewRequest.getProductId());
            return new ResourceNotFoundException("Product Category with Id " + createOrUpdateReviewRequest.getProductId() + " not found!");
        });
        return product.get();
    }
}
