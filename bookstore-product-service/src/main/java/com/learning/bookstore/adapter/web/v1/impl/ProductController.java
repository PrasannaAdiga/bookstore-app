package com.learning.bookstore.adapter.web.v1.impl;

import com.learning.bookstore.adapter.web.v1.IProductController;
import com.learning.bookstore.application.port.in.product.IProductCommandService;
import com.learning.bookstore.application.port.in.product.IProductQueryService;
import com.learning.bookstore.application.port.in.product.request.CreateProductRequest;
import com.learning.bookstore.application.port.in.product.request.UpdateProductRequest;
import com.learning.bookstore.application.port.in.product.response.ProductResponse;
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
public class ProductController implements IProductController {
    private final IProductCommandService productCommandService;
    private final IProductQueryService productQueryService;

    /*============================================ Command Section ===============================================*/

    @Override
    public ResponseEntity<?> createProduct(CreateProductRequest createProductRequest) {
        String productId = productCommandService.createProduct(createProductRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(productId).toUri();
        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<?> updateProduct(UpdateProductRequest updateProductRequest) {
        productCommandService.updateProduct(updateProductRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> deleteProductById(String id) {
        productCommandService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    /*============================================ Query Section ================================================*/

    @Override
    public ResponseEntity<ProductResponse> getProductById(String id) {
        return ResponseEntity.ok().body(productQueryService.getProductById(id));
    }

    @Override
    public ResponseEntity<Page<ProductResponse>> getAllProduct(Integer page, Integer size, String sort) {
        List<ProductResponse> productResponses = productQueryService.getAllProduct(page, size, sort);
        long total = productQueryService.getTotalProduct();
        Page<ProductResponse> productResponsePage = new PageImpl<>(productResponses, PageRequest.of(page, size, Sort.by(sort).ascending()),total);
        return ResponseEntity.ok().body(productResponsePage);
    }

}
