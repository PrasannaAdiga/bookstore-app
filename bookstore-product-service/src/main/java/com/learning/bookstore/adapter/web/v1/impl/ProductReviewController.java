package com.learning.bookstore.adapter.web.v1.impl;

import com.learning.bookstore.adapter.web.v1.IProductReviewController;
import com.learning.bookstore.application.port.in.review.IProductReviewCommandService;
import com.learning.bookstore.application.port.in.review.IProductReviewQueryService;
import com.learning.bookstore.application.port.in.review.request.CreateOrUpdateReviewRequest;
import com.learning.bookstore.application.port.in.review.response.ProductReviewResponse;
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
public class ProductReviewController implements IProductReviewController {
    private final IProductReviewCommandService productReviewCommandService;
    private final IProductReviewQueryService productReviewQueryService;

    /*============================================ Command Section ===============================================*/

    @Override
    public ResponseEntity<?> createOrUpdateReview(CreateOrUpdateReviewRequest createOrUpdateReviewRequest) {
        String productReviewId = productReviewCommandService.createOrUpdateProductReview(createOrUpdateReviewRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(productReviewId).toUri();
        return ResponseEntity.created(location).build();
    }

    /*============================================ Query Section ================================================*/

    @Override
    public ResponseEntity<Page<ProductReviewResponse>> getAllReviewOfProduct(String productId, Integer page, Integer size, String sort) {
        List<ProductReviewResponse> productReviewResponses = productReviewQueryService.getAllReviewOfProduct(productId, page, size, sort);
        long total = productReviewQueryService.getTotalReviewOfProduct(productId);
        Page<ProductReviewResponse> productReviewResponsePage = new PageImpl<>(productReviewResponses, PageRequest.of(page, size, Sort.by(sort).ascending()),total);
        return ResponseEntity.ok().body(productReviewResponsePage);
    }
}
