package com.learning.bookstore.application.service.product;

import com.learning.bookstore.domain.Product;
import com.learning.bookstore.domain.ProductCategory;
import com.learning.bookstore.application.exception.ResourceFoundException;
import com.learning.bookstore.application.exception.ResourceNotFoundException;
import com.learning.bookstore.application.port.in.product.IProductCommandService;
import com.learning.bookstore.application.port.in.product.request.CreateProductRequest;
import com.learning.bookstore.application.port.in.product.request.ProductRequest;
import com.learning.bookstore.application.port.in.product.request.UpdateProductRequest;
import com.learning.bookstore.application.port.out.IProductCategoryPersistenceAdapter;
import com.learning.bookstore.application.port.out.IProductPersistenceAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class ProductCommandService implements IProductCommandService {
    private final IProductPersistenceAdapter productPersistenceAdapter;
    private final IProductCategoryPersistenceAdapter productCategoryPersistenceAdapter;

    @Override
    public String createProduct(CreateProductRequest createProductRequest) {
        productPersistenceAdapter.getProductByName(createProductRequest.getProductName()).ifPresent((product) -> {
            log.error("ResourceFoundException in ProductCommandService.createProduct: Product with name {} already found", product.getName());
            throw new ResourceFoundException("Product with name " + product.getName() + " already found!");
        });
        Product product = buildProduct(createProductRequest);
        return productPersistenceAdapter.createProduct(product).getId();
    }

    @Override
    public void updateProduct(UpdateProductRequest updateProductRequest) {
        productPersistenceAdapter.getProductById(updateProductRequest.getProductId()).orElseThrow(() -> {
            log.error("ResourceNotFoundException in ProductCommandService.updateProduct: Product with id {} not found", updateProductRequest.getProductId());
            throw new ResourceNotFoundException("Product with id " + updateProductRequest.getProductId() + " not found!");
        });
        Product product = buildProduct(updateProductRequest);
        productPersistenceAdapter.updateProduct(product);
    }

    @Override
    public void deleteProductById(String id) {
        productPersistenceAdapter.getProductById(id).orElseThrow(() -> {
            log.error("ResourceNotFoundException in ProductCommandService.deleteProductById: Product with id {} not found", id);
            throw new ResourceNotFoundException("Product with id " + id + " not found!");
        });
        productPersistenceAdapter.deleteProduct(id);
    }

    private Product buildProduct(ProductRequest productRequest) {
        return Product.builder()
                .id(productRequest instanceof UpdateProductRequest ? ((UpdateProductRequest) productRequest).getProductId() : null)
                .name(productRequest.getProductName())
                .description(productRequest.getProductDescription())
                .price(productRequest.getProductPrice())
                .imageId(productRequest.getProductImageId())
                .category(getProductCategory(productRequest))
                .availableCount(productRequest.getAvailableProductCount())
                .build();
    }

    private ProductCategory getProductCategory(ProductRequest productRequest) {
        Optional<ProductCategory> productCategory = productCategoryPersistenceAdapter.getProductCategoryById(productRequest.getProductCategoryId());
        productCategory.orElseThrow(() -> {
            log.error("ResourceNotFoundException in ProductCommandService.getProductCategory: Product Category with Id {} not found", productRequest.getProductCategoryId());
            return new ResourceNotFoundException("Product Category with Id " + productRequest.getProductCategoryId() + " not found!");
        });
        return productCategory.get();
    }

}
