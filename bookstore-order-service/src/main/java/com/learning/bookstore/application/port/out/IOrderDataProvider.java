package com.learning.bookstore.application.port.out;

import com.learning.bookstore.domain.Order;

import java.util.List;
import java.util.Optional;

public interface IOrderDataProvider {
    Order createOrder(Order order);
    Optional<Order> getOrderById(String orderId);
    Optional<List<Order>> getOrderByUserEmail(String userEmail);
    Optional<List<Order>> getAllPlacedOrder();
}
