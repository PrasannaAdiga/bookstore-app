package com.bookstore.learning.application.port.in;

import com.bookstore.learning.application.service.response.OrderResponse;

import java.util.List;

public interface IOrderQueryService {
    OrderResponse getOrderOfUserById(String orderId, String loggedInUserEmail);
    List<OrderResponse> getAllOrderOfUser(String loggedInUserEmail);
    List<OrderResponse> getAllPlacedOrder();
}
