package com.learning.bookstore.adapter.web.v1.impl;

import com.learning.bookstore.adapter.web.v1.IProductCategoryController;
import com.learning.bookstore.application.port.in.category.IProductCategoryCommandService;
import com.learning.bookstore.application.port.in.category.IProductCategoryQueryService;
import com.learning.bookstore.application.port.in.category.request.CreateProductCategoryRequest;
import com.learning.bookstore.application.port.in.category.request.UpdateProductCategoryRequest;
import com.learning.bookstore.application.port.in.category.response.ProductCategoryResponse;
import com.learning.bookstore.infrastructure.annotation.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@WebAdapter
@RequiredArgsConstructor
public class ProductCategoryController implements IProductCategoryController {
    private final IProductCategoryCommandService productCategoryCommandService;
    private final IProductCategoryQueryService productCategoryQueryService;

    /*============================================ Command Section ===============================================*/

    @Override
    public ResponseEntity<?> createProductCategory(CreateProductCategoryRequest createProductCategoryRequest) {
        String productCategoryId = productCategoryCommandService.createProductCategory(createProductCategoryRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(productCategoryId).toUri();
        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<?> updateProductCategory(UpdateProductCategoryRequest updateProductCategoryRequest) {
        productCategoryCommandService.updateProductCategory(updateProductCategoryRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> deleteProductCategoryById(String id) {
        productCategoryCommandService.deleteProductCategoryById(id);
        return ResponseEntity.noContent().build();
    }

    /*============================================ Query Section ================================================*/

    @Override
    public ResponseEntity<ProductCategoryResponse> getProductCategoryById(String id) {
        return ResponseEntity.ok().body(productCategoryQueryService.getProductCategoryById(id));
    }

    @Override
    public ResponseEntity<Page<ProductCategoryResponse>> getAllProductCategory(Integer page, Integer size, String sort) {
        List<ProductCategoryResponse> productCategoryResponses = productCategoryQueryService.getAllProductCategory(page, size, sort);
        long total = productCategoryQueryService.getTotalProductCategory();
        Page<ProductCategoryResponse> productCategoryResponsePage = new PageImpl<>(productCategoryResponses, PageRequest.of(page, size, Sort.by(sort).ascending()),total);
        return ResponseEntity.ok().body(productCategoryResponsePage);
    }

}
