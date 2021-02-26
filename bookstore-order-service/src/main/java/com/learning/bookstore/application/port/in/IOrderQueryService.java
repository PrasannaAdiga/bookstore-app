package com.learning.bookstore.application.port.in;

import com.learning.bookstore.application.service.response.OrderResponse;

import java.util.List;

public interface IOrderQueryService {
    OrderResponse getOrderOfUserById(String orderId);
    List<OrderResponse> getAllOrderOfUser();
    List<OrderResponse> getAllPlacedOrder();
}
