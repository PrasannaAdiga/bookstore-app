package com.bookstore.learning.application.port.in;

import com.bookstore.learning.application.service.request.CreateOrderRequest;
import com.bookstore.learning.application.service.request.PreviewOrderRequest;
import com.bookstore.learning.application.service.response.OrderResponse;
import com.bookstore.learning.application.service.response.PreviewOrderResponse;

public interface IOrderCommandService {
    OrderResponse createOrderForLoggedInUser(CreateOrderRequest createOrderRequest, String loggedInUserEmail);
    PreviewOrderResponse previewOrderOfLoggedInUser(PreviewOrderRequest previewOrderRequest, String loggedInUserEmail);
}
