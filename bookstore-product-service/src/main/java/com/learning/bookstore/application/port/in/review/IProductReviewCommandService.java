package com.learning.bookstore.application.port.in.review;

import com.learning.bookstore.application.port.in.review.request.CreateOrUpdateReviewRequest;

public interface IProductReviewCommandService {
    String createOrUpdateProductReview(CreateOrUpdateReviewRequest createOrUpdateReviewRequest);
}
