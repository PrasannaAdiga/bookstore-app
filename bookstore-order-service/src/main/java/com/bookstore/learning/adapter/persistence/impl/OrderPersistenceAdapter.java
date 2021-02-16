package com.bookstore.learning.adapter.persistence.impl;

import com.bookstore.learning.adapter.persistence.entity.OrderEntity;
import com.bookstore.learning.adapter.persistence.entity.OrderItemEntity;
import com.bookstore.learning.adapter.persistence.repository.OrderRepository;
import com.bookstore.learning.application.port.out.IOrderDataProvider;
import com.bookstore.learning.domain.Order;
import com.bookstore.learning.domain.OrderItem;
import com.bookstore.learning.infrastructure.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements IOrderDataProvider {
    private final OrderRepository orderRepository;

    @Override
    public Order createOrder(Order order) {
        OrderEntity orderEntity = buildOrderEntity(order);
        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);
        return buildOrder(savedOrderEntity);
    }

    @Override
    public Optional<Order> getOrderById(String orderId) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if(!orderEntity.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(buildOrder(orderEntity.get()));
    }

    @Override
    public Optional<List<Order>> getOrderByUserEmail(String userEmail) {
        Optional<List<OrderEntity>> orderEntities = orderRepository.findByUserMail(userEmail);
        if(!orderEntities.isPresent()) {
            return Optional.empty();
        }
        List<Order> orders = new ArrayList<>();
        orderEntities.get().forEach(orderEntity -> {
            orders.add(buildOrder(orderEntity));
        });
        return Optional.of(orders);
    }

    @Override
    public Optional<List<Order>> getAllPlacedOrder() {
        List<OrderEntity> orderEntities = orderRepository.findAll();
        List<Order> orders = new ArrayList<>();
        orderEntities.forEach(orderEntity -> {
            orders.add(buildOrder(orderEntity));
        });
        return Optional.of(orders);
    }

    private OrderEntity buildOrderEntity(Order order) {
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        order.getOrderItems().forEach(orderItem -> {
            orderItemEntities.add(buildOrderItemEntity(orderItem));
        });
        return OrderEntity.builder()
                .orderItems(orderItemEntities)
                .totalOrderPrice(order.getTotalOrderPrice())
                .billingAddressId(order.getBillingAddressId())
                .deliveredDate(order.getDeliveredDate())
                .isDelivered(order.isDelivered())
                .isPaid(order.isPaid())
                .paymentDate(order.getPaymentDate())
                .paymentId(order.getPaymentId())
                .paymentMethodId(order.getPaymentMethodId())
                .paymentReceiptUrl(order.getPaymentReceiptUrl())
                .shippingAddressId(order.getShippingAddressId())
                .shippingPrice(order.getShippingPrice())
                .taxPrice(order.getTaxPrice())
                .totalItemsPrice(order.getTotalItemsPrice())
                .userMail(order.getUserEmail())
                .build();
    }

    private OrderItemEntity buildOrderItemEntity(OrderItem orderItem) {
        return  OrderItemEntity.builder()
                .id(orderItem.getId())
                .orderExtendedPrice(orderItem.getOrderExtendedPrice())
                .orderItemPrice(orderItem.getOrderItemPrice())
                .productId(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .build();
    }

    private Order buildOrder(OrderEntity orderEntity) {
        List<OrderItem> orderItems = new ArrayList<>();
        orderEntity.getOrderItems().forEach(orderItem -> {
            orderItems.add(buildOrderItem(orderItem));
        });
        return Order.builder()
                .orderItems(orderItems)
                .totalOrderPrice(orderEntity.getTotalOrderPrice())
                .billingAddressId(orderEntity.getBillingAddressId())
                .deliveredDate(orderEntity.getDeliveredDate())
                .isDelivered(orderEntity.isDelivered())
                .isPaid(orderEntity.isPaid())
                .paymentDate(orderEntity.getPaymentDate())
                .paymentId(orderEntity.getPaymentId())
                .paymentMethodId(orderEntity.getPaymentMethodId())
                .paymentReceiptUrl(orderEntity.getPaymentReceiptUrl())
                .shippingAddressId(orderEntity.getShippingAddressId())
                .shippingPrice(orderEntity.getShippingPrice())
                .taxPrice(orderEntity.getTaxPrice())
                .totalItemsPrice(orderEntity.getTotalItemsPrice())
                .userEmail(orderEntity.getUserMail())
                .build();
    }

    private OrderItem buildOrderItem(OrderItemEntity orderItemEntity) {
        return  OrderItem.builder()
                .id(orderItemEntity.getId())
                .orderExtendedPrice(orderItemEntity.getOrderExtendedPrice())
                .orderItemPrice(orderItemEntity.getOrderItemPrice())
                .productId(orderItemEntity.getProductId())
                .quantity(orderItemEntity.getQuantity())
                .build();
    }


}
