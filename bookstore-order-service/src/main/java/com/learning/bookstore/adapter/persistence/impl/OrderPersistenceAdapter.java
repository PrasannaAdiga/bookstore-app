package com.learning.bookstore.adapter.persistence.impl;

import com.learning.bookstore.adapter.persistence.entity.OrderEntity;
import com.learning.bookstore.adapter.persistence.entity.OrderItemEntity;
import com.learning.bookstore.adapter.persistence.repository.OrderRepository;
import com.learning.bookstore.application.port.out.IOrderDataProvider;
import com.learning.bookstore.domain.Order;
import com.learning.bookstore.domain.OrderItem;
import com.learning.bookstore.infrastructure.annotation.PersistenceAdapter;
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
        if(!orderEntities.isPresent() || orderEntities.get().isEmpty()) {
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
        if(orderEntities.isEmpty()) {
            return Optional.empty();
        }
        List<Order> orders = new ArrayList<>();
        orderEntities.forEach(orderEntity -> {
            orders.add(buildOrder(orderEntity));
        });
        return Optional.of(orders);
    }

    private OrderEntity buildOrderEntity(Order order) {
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        OrderEntity orderEntity = OrderEntity.builder()
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
        order.getOrderItems().forEach(orderItem -> {
            orderItemEntities.add(buildOrderItemEntity(orderItem, orderEntity));
        });
        orderEntity.setOrderItems(orderItemEntities);
        return orderEntity;
    }

    private OrderItemEntity buildOrderItemEntity(OrderItem orderItem, OrderEntity orderEntity) {
        return  OrderItemEntity.builder()
                .id(orderItem.getId())
                .order(orderEntity)
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
                .id(orderEntity.getId())
                .creationDate(orderEntity.getCreationDate())
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
