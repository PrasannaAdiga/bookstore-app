package com.learning.bookstore.application.service.category;

import com.learning.bookstore.application.exception.ResourceNotFoundException;
import com.learning.bookstore.domain.ProductCategory;
import com.learning.bookstore.application.exception.ResourceFoundException;
import com.learning.bookstore.application.port.in.category.IProductCategoryCommandService;
import com.learning.bookstore.application.port.in.category.request.CreateProductCategoryRequest;
import com.learning.bookstore.application.port.in.category.request.ProductCategoryRequest;
import com.learning.bookstore.application.port.in.category.request.UpdateProductCategoryRequest;
import com.learning.bookstore.application.port.out.IProductCategoryPersistenceAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class ProductCategoryCommandService implements IProductCategoryCommandService {
    private final IProductCategoryPersistenceAdapter productCategoryPersistenceAdapter;

    @Override
    public String createProductCategory(CreateProductCategoryRequest createProductCategoryRequest) {
        productCategoryPersistenceAdapter.getProductCategoryByName(createProductCategoryRequest.getProductCategoryName()).ifPresent((pCategory) -> {
            log.error("ResourceFoundException in ProductCategoryCommandService.createProductCategory: Product category with name {} already found", pCategory.getName());
            throw new ResourceFoundException("Product category with name " + pCategory.getName() + " already found!");
        });
        ProductCategory productCategory = mapToProductCategory(createProductCategoryRequest);
        return productCategoryPersistenceAdapter.createProductCategory(productCategory).getId();
    }

    @Override
    public void updateProductCategory(UpdateProductCategoryRequest updateProductCategoryRequest) {
        productCategoryPersistenceAdapter.getProductCategoryById(updateProductCategoryRequest.getProductCategoryId()).orElseThrow(() -> {
            log.error("ResourceNotFoundException in ProductCategoryCommandService.updateProductCategory: Product category with id {} not found", updateProductCategoryRequest.getProductCategoryId());
            throw new ResourceNotFoundException("Product category with id " + updateProductCategoryRequest.getProductCategoryId() + " not found!");
        });
        ProductCategory productCategory = mapToProductCategory(updateProductCategoryRequest);
        productCategoryPersistenceAdapter.updateProductCategory(productCategory);
    }

    @Override
    public void deleteProductCategoryById(String id) {
        productCategoryPersistenceAdapter.getProductCategoryById(id).orElseThrow(() -> {
            log.error("ResourceNotFoundException in ProductCategoryCommandService.deleteProductCategoryById: Product category with id {} not found", id);
            throw new ResourceNotFoundException("Product category with id " + id + " not found!");
        });
        productCategoryPersistenceAdapter.deleteProductCategoryById(id);
    }

    private ProductCategory mapToProductCategory(ProductCategoryRequest productCategoryRequest) {
        return ProductCategory.builder()
                .id(productCategoryRequest instanceof UpdateProductCategoryRequest ? ((UpdateProductCategoryRequest)productCategoryRequest).getProductCategoryId() : null)
                .name(productCategoryRequest.getProductCategoryName())
                .description(productCategoryRequest.getProductCategoryDescription())
                .build();
    }
}
