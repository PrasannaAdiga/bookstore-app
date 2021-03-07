package com.learning.bookstore.application.service;

import com.learning.bookstore.application.exception.InvalidPaymentMethodException;
import com.learning.bookstore.application.exception.ResourceNotFoundException;
import com.learning.bookstore.application.exception.UnauthorizedException;
import com.learning.bookstore.application.port.in.IOrderQueryService;
import com.learning.bookstore.application.port.out.IOrderDataProvider;
import com.learning.bookstore.application.service.response.CardResponse;
import com.learning.bookstore.application.service.response.OrderItemResponse;
import com.learning.bookstore.application.service.response.OrderResponse;
import com.learning.bookstore.client.AddressFeignClient;
import com.learning.bookstore.client.PaymentFeignClient;
import com.learning.bookstore.domain.Order;
import com.learning.bookstore.infrastructure.util.PrincipalResolver;
import com.learning.bookstore.web.BillingAddressResponse;
import com.learning.bookstore.web.PaymentMethodResponse;
import com.learning.bookstore.web.ShippingAddressResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class OrderQueryService implements IOrderQueryService {
    private final IOrderDataProvider orderDataProvider;
    private final AddressFeignClient addressFeignClient;
    private final PaymentFeignClient paymentFeignClient;
    private final PrincipalResolver principalResolver;

    @Override
    public OrderResponse getOrderOfUserById(String orderId) {
        String loggedInUserEmail = principalResolver.getCurrentLoggedInUserMail();
        String loggedInUserToken = principalResolver.getCurrentLoggedInUserToken();
        Optional<Order> order = orderDataProvider.getOrderById(orderId);
        order.orElseThrow(() -> {
            log.error("ResourceNotFoundException in OrderQueryService.getOrderOfUserById: No order found with id {}", orderId);
            throw new ResourceNotFoundException("No order found with id " + orderId);
        });
        if(!order.get().getUserEmail().equalsIgnoreCase(loggedInUserEmail)) {
            log.error("UnauthorizedException in OrderQueryService.getOrderOfUserById: Order doesn't belong to the user {}", loggedInUserEmail);
            throw new UnauthorizedException("Order doesn't belong to the user " + loggedInUserEmail);
        }
        //Set card details
        CardResponse card = null;
        try {
            PaymentMethodResponse paymentMethodResponse = paymentFeignClient.getPaymentMethodOfUserById("Bearer " + loggedInUserToken, order.get().getPaymentMethodId());
            card = CardResponse.builder()
                    .last4Digits(paymentMethodResponse.getCardLast4Digits())
                    .cardBrand(paymentMethodResponse.getCardType())
                    .paymentMethodId(paymentMethodResponse.getPaymentMethodId())
                    .build();
        } catch (Exception ex){
            log.error("InvalidPaymentMethodException in OrderQueryService.getOrderOfUserById: Invalid payment method with Id {}, Exception: {}", order.get().getPaymentMethodId(), ex);
            throw new InvalidPaymentMethodException("Invalid payment method with Id " + order.get().getPaymentMethodId());
        }

        //set billing and shipping address
        BillingAddressResponse billingAddress = addressFeignClient.getBillingAddressById("Bearer " + loggedInUserToken, order.get().getBillingAddressId());
        ShippingAddressResponse shippingAddress = addressFeignClient.getShippingAddressById("Bearer " + loggedInUserToken, order.get().getShippingAddressId());
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        order.get().getOrderItems().forEach(orderItem -> {
            orderItemResponses.add(OrderItemResponse.builder()
                    .id(orderItem.getId())
                    .productId(orderItem.getProductId())
                    .quantity(orderItem.getQuantity())
                    .orderItemPrice(orderItem.getOrderItemPrice())
                    .orderExtendedPrice(orderItem.getOrderExtendedPrice())
                    .build());
        });
        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(orderId)
                .orderItems(orderItemResponses)
                .billingAddress(billingAddress)
                .shippingAddress(shippingAddress)
                .shippingPrice(order.get().getShippingPrice())
                .card(card)
                .isDelivered(order.get().isDelivered())
                .isPaid(order.get().isPaid())
                .itemsTotalPrice(order.get().getTotalItemsPrice())
                .paymentDate(order.get().getPaymentDate())
                .paymentReceiptUrl(order.get().getPaymentReceiptUrl())
                .taxPrice(order.get().getTaxPrice())
                .totalPrice(order.get().getTotalOrderPrice())
                .build();
        return orderResponse;
    }

    @Override
    public List<OrderResponse> getAllOrderOfUser() {
        String loggedInUserEmail = principalResolver.getCurrentLoggedInUserMail();
        Optional<List<Order>> order = orderDataProvider.getOrderByUserEmail(loggedInUserEmail);
        order.orElseThrow(() -> {
            log.error("ResourceNotFoundException in OrderQueryService.getOrderOfUserById: No order found for the user email {}", loggedInUserEmail);
            throw new ResourceNotFoundException("No order found for the user email " + loggedInUserEmail);
        });
        return buildOrderResponse(order.get());
    }

    @Override
    public List<OrderResponse> getAllPlacedOrder() {
        Optional<List<Order>> order = orderDataProvider.getAllPlacedOrder();
        order.orElseThrow(() -> {
            log.error("ResourceNotFoundException in OrderQueryService.getOrderOfUserById: No order found for the user email {}", principalResolver.getCurrentLoggedInUserMail());
            throw new ResourceNotFoundException("No order found for the user email " + principalResolver.getCurrentLoggedInUserMail());
        });
        return buildOrderResponse(order.get());
    }

    private List<OrderResponse> buildOrderResponse(List<Order> orders) {
        String loggedInUserToken = principalResolver.getCurrentLoggedInUserToken();
        List<OrderResponse> orderResponses = new ArrayList<>();
        orders.forEach(order -> {
            String orderId = order.getId();
            BillingAddressResponse billingAddress = addressFeignClient.getBillingAddressById("Bearer " + loggedInUserToken, order.getBillingAddressId());
            ShippingAddressResponse shippingAddress = addressFeignClient.getShippingAddressById("Bearer " + loggedInUserToken, order.getShippingAddressId());
            //Set card details
            CardResponse card = null;
            try {
                PaymentMethodResponse paymentMethodResponse = paymentFeignClient.getPaymentMethodOfUserById("Bearer " + loggedInUserToken, order.getPaymentMethodId());
                card = CardResponse.builder()
                        .last4Digits(paymentMethodResponse.getCardLast4Digits())
                        .cardBrand(paymentMethodResponse.getCardType())
                        .paymentMethodId(paymentMethodResponse.getPaymentMethodId())
                        .build();
            } catch (Exception ex){
                log.error("InvalidPaymentMethodException in OrderQueryService.getOrderOfUserById: Invalid payment method with Id {}, Exception: {}", order.getPaymentMethodId(), ex);
                throw new InvalidPaymentMethodException("Invalid payment method with Id " + order.getPaymentMethodId());
            }
            List<OrderItemResponse> orderItemResponses = new ArrayList<>();
            order.getOrderItems().forEach(orderItem -> {
                orderItemResponses.add(OrderItemResponse.builder()
                        .id(orderItem.getId())
                        .productId(orderItem.getProductId())
                        .quantity(orderItem.getQuantity())
                        .orderItemPrice(orderItem.getOrderItemPrice())
                        .orderExtendedPrice(orderItem.getOrderExtendedPrice())
                        .build());
            });
            OrderResponse orderResponse = OrderResponse.builder()
                    .orderId(orderId)
                    .orderItems(orderItemResponses)
                    .billingAddress(billingAddress)
                    .shippingAddress(shippingAddress)
                    .shippingPrice(order.getShippingPrice())
                    .card(card)
                    .isDelivered(order.isDelivered())
                    .isPaid(order.isPaid())
                    .itemsTotalPrice(order.getTotalItemsPrice())
                    .paymentDate(order.getPaymentDate())
                    .paymentReceiptUrl(order.getPaymentReceiptUrl())
                    .taxPrice(order.getTaxPrice())
                    .totalPrice(order.getTotalOrderPrice())
                    .created_at(order.getCreationDate())
                    .build();
            orderResponses.add(orderResponse);
        });
        return orderResponses;
    }

}
