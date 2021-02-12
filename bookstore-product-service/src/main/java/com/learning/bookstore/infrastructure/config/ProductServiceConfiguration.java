package com.learning.bookstore.infrastructure.config;

import com.learning.bookstore.application.port.out.IProductCategoryPersistenceAdapter;
import com.learning.bookstore.application.port.out.IProductPersistenceAdapter;
import com.learning.bookstore.application.port.out.IProductReviewPersistenceAdapter;
import com.learning.bookstore.application.service.category.ProductCategoryCommandService;
import com.learning.bookstore.application.service.category.ProductCategoryQueryService;
import com.learning.bookstore.application.service.image.ProductImageService;
import com.learning.bookstore.application.service.product.ProductCommandService;
import com.learning.bookstore.application.service.product.ProductQueryService;
import com.learning.bookstore.application.service.review.ProductReviewCommandService;
import com.learning.bookstore.application.service.review.ProductReviewQueryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductServiceConfiguration {
    @Bean
    public ProductCommandService productCommandService(IProductPersistenceAdapter productPersistenceAdapter,
                                                       IProductCategoryPersistenceAdapter productCategoryPersistenceAdapter) {
        return new ProductCommandService(productPersistenceAdapter, productCategoryPersistenceAdapter);
    }

    @Bean
    public ProductQueryService productQueryService(IProductPersistenceAdapter productPersistenceAdapter,
                                                   IProductReviewPersistenceAdapter productReviewPersistenceAdapter) {
        return new ProductQueryService(productPersistenceAdapter, productReviewPersistenceAdapter);
    }

    @Bean
    public ProductCategoryCommandService productCategoryCommandService(IProductCategoryPersistenceAdapter productCategoryPersistenceAdapter) {
        return new ProductCategoryCommandService(productCategoryPersistenceAdapter);
    }

    @Bean
    public ProductCategoryQueryService productCategoryQueryService(IProductCategoryPersistenceAdapter productCategoryPersistenceAdapter){
        return new ProductCategoryQueryService(productCategoryPersistenceAdapter);
    }

    @Bean
    public ProductReviewCommandService productReviewCommandService(IProductReviewPersistenceAdapter productReviewPersistenceAdapter, IProductPersistenceAdapter productPersistenceAdapter) {
        return new ProductReviewCommandService(productReviewPersistenceAdapter, productPersistenceAdapter);
    }

    @Bean
    public ProductReviewQueryService productReviewQueryService(IProductReviewPersistenceAdapter productReviewPersistenceAdapter) {
        return new ProductReviewQueryService(productReviewPersistenceAdapter);
    }

    @Bean
    public ProductImageService productImageService() {
        return new ProductImageService();
    }

}
