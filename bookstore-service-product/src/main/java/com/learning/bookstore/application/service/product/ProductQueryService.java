package com.learning.bookstore.application.service.product;

import com.learning.bookstore.domain.Product;
import com.learning.bookstore.application.exception.ResourceNotFoundException;
import com.learning.bookstore.application.port.in.product.IProductQueryService;
import com.learning.bookstore.application.port.in.product.response.ProductResponse;
import com.learning.bookstore.application.port.out.IProductPersistenceAdapter;
import com.learning.bookstore.application.port.out.IProductReviewPersistenceAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class ProductQueryService implements IProductQueryService {
    private final IProductPersistenceAdapter productPersistenceAdapter;
    private final IProductReviewPersistenceAdapter productReviewPersistenceAdapter;

    @Override
    public ProductResponse getProductById(String id) {
        Optional<Product> oProduct = productPersistenceAdapter.getProductById(id);
        oProduct.orElseThrow(() -> {
            log.error("ResourceNotFoundException in ProductQueryService.getProductById: Product with Id {} not found", id);
            return new ResourceNotFoundException("Product with Id " + id + " not found!");
        });
        Product product = oProduct.get();
        List<Integer> productReviewRatings = productReviewPersistenceAdapter.getAllReviewRatingOfProduct(id);
        return buildProductResponse(product, productReviewRatings);
    }

    @Override
    public List<ProductResponse> getAllProduct(Integer page, Integer size, String sort) {
        List<Product> products = productPersistenceAdapter.getAllProduct(page, size, sort);
        return products.stream().map(new Function<Product, ProductResponse>() {
            @Override
            public ProductResponse apply(Product product) {
                List<Integer> productReviewRatings = productReviewPersistenceAdapter.getAllReviewRatingOfProduct(product.getId());
                return buildProductResponse(product, productReviewRatings);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public long getTotalProduct() {
        return productPersistenceAdapter.getTotalProduct();
    }

    private ProductResponse buildProductResponse(Product product, List<Integer> productReviewRatings) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageId(product.getImageId())
                .categoryId(product.getCategory().getId())
                .availableCount(product.getAvailableCount())
                .averageRating(getAverageRating(productReviewRatings))
                .noOfRatings(productReviewRatings.size())
                .build();
    }

    private double getAverageRating(List<Integer> productReviewRatings) {
        double rating = 0;
        if (productReviewRatings.size() > 0) {
            double sum = productReviewRatings.stream().mapToDouble(Integer::intValue).sum();
            rating = sum / productReviewRatings.size();
        }
        return rating;
    }

}
