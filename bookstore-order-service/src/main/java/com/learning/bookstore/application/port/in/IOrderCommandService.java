package com.learning.bookstore.application.port.in;

import com.learning.bookstore.application.service.request.CreateOrderRequest;
import com.learning.bookstore.application.service.request.PreviewOrderRequest;
import com.learning.bookstore.application.service.response.OrderResponse;
import com.learning.bookstore.application.service.response.PreviewOrderResponse;

public interface IOrderCommandService {
    OrderResponse createOrderForLoggedInUser(CreateOrderRequest createOrderRequest);
    PreviewOrderResponse previewOrderOfLoggedInUser(PreviewOrderRequest previewOrderRequest);
}
