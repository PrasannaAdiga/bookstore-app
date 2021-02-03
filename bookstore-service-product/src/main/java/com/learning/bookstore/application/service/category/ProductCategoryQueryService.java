package com.learning.bookstore.application.service.category;

import com.learning.bookstore.domain.ProductCategory;
import com.learning.bookstore.application.exception.ResourceNotFoundException;
import com.learning.bookstore.application.port.in.category.IProductCategoryQueryService;
import com.learning.bookstore.application.port.in.category.response.ProductCategoryResponse;
import com.learning.bookstore.application.port.out.IProductCategoryPersistenceAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class ProductCategoryQueryService implements IProductCategoryQueryService {
    private final IProductCategoryPersistenceAdapter productCategoryPersistenceAdapter;

    @Override
    public ProductCategoryResponse getProductCategoryById(String id) {
        Optional<ProductCategory> oProductCategory = productCategoryPersistenceAdapter.getProductCategoryById(id);
        oProductCategory.orElseThrow(() -> {
            log.error("ResourceNotFoundException in ProductCategoryQueryService.getProductCategoryById: Product category with Id {} not found", id);
            return new ResourceNotFoundException("Product category with Id " + id + " not found!");
        });
        ProductCategory productCategory = oProductCategory.get();
        return buildProductCategoryResponse(productCategory);
    }

    @Override
    public List<ProductCategoryResponse> getAllProductCategory(Integer page, Integer size, String sort) {
        List<ProductCategory> productCategory = productCategoryPersistenceAdapter.getAllProductCategory(page, size, sort);
        return productCategory.stream().map(new Function<ProductCategory, ProductCategoryResponse>() {
            @Override
            public ProductCategoryResponse apply(ProductCategory pCategory) {
                return buildProductCategoryResponse(pCategory);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public long getTotalProductCategory() {
        return productCategoryPersistenceAdapter.getTotalProductCategory();
    }

    private ProductCategoryResponse buildProductCategoryResponse(ProductCategory pCategory) {
        return ProductCategoryResponse.builder()
                .id(pCategory.getId())
                .name(pCategory.getName())
                .description(pCategory.getDescription())
                .build();
    }

}
