package com.learning.bookstore.application.service;

import com.learning.bookstore.application.exception.EmptyCartException;
import com.learning.bookstore.application.exception.EmptyShippingAddressException;
import com.learning.bookstore.application.exception.InvalidPaymentMethodException;
import com.learning.bookstore.application.exception.ResourceNotFoundException;
import com.learning.bookstore.application.port.in.IOrderCommandService;
import com.learning.bookstore.application.port.out.IOrderDataProvider;
import com.learning.bookstore.application.service.request.CreateOrderRequest;
import com.learning.bookstore.application.service.request.PreviewOrderRequest;
import com.learning.bookstore.application.service.response.CardResponse;
import com.learning.bookstore.application.service.response.OrderResponse;
import com.learning.bookstore.application.service.response.OrderItemResponse;
import com.learning.bookstore.application.service.response.PreviewOrderResponse;
import com.learning.bookstore.client.AddressFeignClient;
import com.learning.bookstore.client.CartFeignClient;
import com.learning.bookstore.client.PaymentFeignClient;
import com.learning.bookstore.domain.Order;
import com.learning.bookstore.domain.OrderItem;
import com.learning.bookstore.infrastructure.util.PrincipalResolver;
import com.learning.bookstore.web.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class OrderCommandService implements IOrderCommandService {
    private final IOrderDataProvider orderDataProvider;
    private final AddressFeignClient addressFeignClient;
    private final CartFeignClient cartFeignClient;
    private final PaymentFeignClient paymentFeignClient;
    private final PrincipalResolver principalResolver;

    @Override
    public OrderResponse createOrderForLoggedInUser(CreateOrderRequest request) {
        String loggedInUserEmail = principalResolver.getCurrentLoggedInUserMail();
        String loggedInUserToken = principalResolver.getCurrentLoggedInUserToken();
        OrderResponse orderResponse = new OrderResponse();

        //Get billing address of logged in user
        BillingAddressResponse billingAddressResponse = null;
        if(request.getBillingAddressId() != null && !request.getBillingAddressId().isEmpty()) {
            billingAddressResponse = addressFeignClient.getBillingAddressById("Bearer " + loggedInUserToken, request.getBillingAddressId());
            orderResponse.setBillingAddress(billingAddressResponse);
        }
        //Get shipping address of logged in user
        ShippingAddressResponse shippingAddressResponse = null;
        if(request.getShippingAddressId() != null && !request.getShippingAddressId().isEmpty()) {
            shippingAddressResponse = addressFeignClient.getShippingAddressById("Bearer " + loggedInUserToken, request.getShippingAddressId());
            if(shippingAddressResponse == null) {
                log.error("EmptyShippingAddressException in OrderCommandService.createOrderForLoggedInUser: No shipping address defined for the user email {}", loggedInUserEmail);
                throw new EmptyShippingAddressException("No shipping address defined for the user email " + loggedInUserEmail);
            }
            orderResponse.setShippingAddress(shippingAddressResponse);
            if(orderResponse.getBillingAddress() == null) {
                BillingAddressResponse billingAddress = BillingAddressResponse.builder()
                        .id(shippingAddressResponse.getId())
                        .addressLine1(shippingAddressResponse.getAddressLine1())
                        .addressLine2(shippingAddressResponse.getAddressLine2())
                        .city(shippingAddressResponse.getCity())
                        .country(shippingAddressResponse.getCountry())
                        .phone(shippingAddressResponse.getPhone())
                        .state(shippingAddressResponse.getState())
                        .postalCode(shippingAddressResponse.getPostalCode())
                        .userEmail(shippingAddressResponse.getUserEmail())
                        .build();
                orderResponse.setBillingAddress(billingAddress);
            }
        }
        //Get Cart details
        CartResponse cartResponse = cartFeignClient.getCart("Bearer " + loggedInUserToken);
        if(cartResponse == null || cartResponse.getCartItems() == null || cartResponse.getCartItems().isEmpty()) {
            log.error("EmptyCartException in OrderCommandService.createOrderForLoggedInUser: No Cart with items found for the user email {}", loggedInUserEmail);
            throw new EmptyCartException("No Cart with items found for the user email " + loggedInUserEmail);
        }
        //Create Order and Order Items
        Order order = new Order();
        order.setUserEmail(loggedInUserEmail);
        order.setBillingAddressId(billingAddressResponse.getId());
        order.setShippingAddressId(shippingAddressResponse.getId());
        order.setDeliveredDate(LocalDateTime.now());
        cartResponse.getCartItems().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setOrderItemPrice(cartItem.getPrice());
            orderItem.setOrderExtendedPrice(cartItem.getExtendedPrice());
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            order.getOrderItems().add(orderItem);
        });
        //Set totalItemPrice of Order
        double itemsPrice = order.getOrderItems().stream().mapToDouble(OrderItem::getOrderExtendedPrice).sum();
        orderResponse.setItemsTotalPrice(itemsPrice);
        order.setTotalItemsPrice(itemsPrice);
        //Hardcore tax price of an order to 10%
        Double taxPrice = (itemsPrice * 10) / 100;
        orderResponse.setTaxPrice(taxPrice);
        order.setTaxPrice(taxPrice);
        //Hardcore shipping price of an order to 10%
        Double shippingPrice = 10D;
        orderResponse.setShippingPrice(shippingPrice);
        order.setShippingPrice(shippingPrice);
        //Set total price of an order
        double totalPrice = itemsPrice + taxPrice + shippingPrice;
        orderResponse.setTotalPrice(totalPrice);
        order.setTotalOrderPrice(totalPrice);
        //Do Payment of the order
        CreatePaymentRequest createPaymentRequest = CreatePaymentRequest.builder()
                .amount((int)totalPrice)
                .currency("inr")
                .paymentMethodId(request.getPaymentMethodId())
                .build();
        PaymentResponse paymentResponse = paymentFeignClient.doPayment("Bearer " + loggedInUserToken, createPaymentRequest);
        //Update order with payment details
        order.setPaid(paymentResponse.isCaptured());
        order.setPaymentDate(paymentResponse.getPaymentDate());
        order.setPaymentId(paymentResponse.getPaymentId());
        order.setPaymentReceiptUrl(paymentResponse.getReceipt_url());
        order.setPaymentMethodId(request.getPaymentMethodId());
        //Save the order in DB
        Order savedOrder = orderDataProvider.createOrder(order);
        //Update OrderResponse after Order is saved in DB
        orderResponse.setOrderId(savedOrder.getId());
        savedOrder.getOrderItems().forEach(orderItem -> {
            OrderItemResponse orderItemResponse = OrderItemResponse.builder()
                    .id(orderItem.getId())
                    .orderItemPrice(orderItem.getOrderItemPrice())
                    .orderExtendedPrice(orderItem.getOrderExtendedPrice())
                    .productId(orderItem.getProductId())
                    .quantity(orderItem.getQuantity())
                    .build();
            orderResponse.getOrderItems().add(orderItemResponse);
        });
        orderResponse.setCreated_at(savedOrder.getCreationDate());
        orderResponse.setDeliveredDate(savedOrder.getDeliveredDate());
        orderResponse.setPaid(paymentResponse.isCaptured());
        orderResponse.setPaymentDate(paymentResponse.getPaymentDate());
        orderResponse.setPaymentReceiptUrl(paymentResponse.getReceipt_url());
        //Clear cart
        cartFeignClient.removeAllCartItem("Bearer " + loggedInUserToken, cartResponse.getId());
        return orderResponse;
    }

    @Override
    public PreviewOrderResponse previewOrderOfLoggedInUser(PreviewOrderRequest request) {
        String loggedInUserEmail = principalResolver.getCurrentLoggedInUserMail();
        String loggedInUserToken = principalResolver.getCurrentLoggedInUserToken();
        PreviewOrderResponse previewOrderResponse = new PreviewOrderResponse();
        //Get billing address of logged in user
        BillingAddressResponse billingAddressResponse = null;
        if(request.getBillingAddressId() != null && !request.getBillingAddressId().isEmpty()) {
            billingAddressResponse = addressFeignClient.getBillingAddressById("Bearer " + loggedInUserToken, request.getBillingAddressId());
            previewOrderResponse.setBillingAddress(billingAddressResponse);
        }
        //Get shipping address of logged in user
        ShippingAddressResponse shippingAddressResponse = null;
        if(request.getShippingAddressId() != null && !request.getShippingAddressId().isEmpty()) {
            shippingAddressResponse = addressFeignClient.getShippingAddressById("Bearer " + loggedInUserToken, request.getShippingAddressId());
            previewOrderResponse.setShippingAddress(shippingAddressResponse);
            if(previewOrderResponse.getBillingAddress() == null) {
                BillingAddressResponse billingAddress = BillingAddressResponse.builder()
                        .id(shippingAddressResponse.getId())
                        .addressLine1(shippingAddressResponse.getAddressLine1())
                        .addressLine2(shippingAddressResponse.getAddressLine2())
                        .city(shippingAddressResponse.getCity())
                        .country(shippingAddressResponse.getCountry())
                        .phone(shippingAddressResponse.getPhone())
                        .state(shippingAddressResponse.getState())
                        .postalCode(shippingAddressResponse.getPostalCode())
                        .userEmail(shippingAddressResponse.getUserEmail())
                        .build();
                previewOrderResponse.setBillingAddress(billingAddress);
            }
        }
        //Set Card details
        try {
            PaymentMethodResponse paymentMethodResponse = paymentFeignClient.getPaymentMethodOfUserById("Bearer " + loggedInUserToken, request.getPaymentMethodId());
            CardResponse card = CardResponse.builder()
                    .last4Digits(paymentMethodResponse.getCardLast4Digits())
                    .cardBrand(paymentMethodResponse.getCardType())
                    .paymentMethodId(paymentMethodResponse.getPaymentMethodId())
                    .build();
            previewOrderResponse.setCard(card);
        } catch (Exception ex){
            log.error("InvalidPaymentMethodException in OrderCommandService.previewOrderOfLoggedInUser: Invalid payment method with Id {}, Exception: {}", request.getPaymentMethodId(), ex);
            throw new InvalidPaymentMethodException("Invalid payment method with Id " + request.getPaymentMethodId());
        }
        //Set Order Items
        Optional<List<Order>> orders = orderDataProvider.getOrderByUserEmail(principalResolver.getCurrentLoggedInUserMail());
        orders.orElseThrow(() -> {
            log.error("ResourceNotFoundException in OrderCommandService.previewOrderOfLoggedInUser: No order found for the user email {}", loggedInUserEmail);
            throw new ResourceNotFoundException("No order found for the user email " + loggedInUserEmail);
        });
        orders.get().forEach(order -> {
            order.getOrderItems().forEach(orderItem -> {
                previewOrderResponse.getOrderItems().add(OrderItemResponse.builder()
                        .orderExtendedPrice(orderItem.getOrderExtendedPrice())
                        .orderItemPrice(orderItem.getOrderItemPrice())
                        .productId(orderItem.getProductId())
                        .quantity(orderItem.getQuantity())
                        .id(orderItem.getId())
                        .build());
            });
        });
        //Set totalItemPrice of Order
        double itemsPrice = previewOrderResponse.getOrderItems().stream().mapToDouble(OrderItemResponse::getOrderExtendedPrice).sum();
        previewOrderResponse.setItemsTotalPrice(itemsPrice);
        //Hardcore tax price of an order to 10%
        Double taxPrice = (itemsPrice * 10 ) / 100;
        previewOrderResponse.setTaxPrice(taxPrice);
        //Hardcore shipping price of an order to 10%
        Double shippingPrice = 10D;
        previewOrderResponse.setShippingPrice(shippingPrice);
        previewOrderResponse.setTotalPrice(itemsPrice + taxPrice + shippingPrice);
        return previewOrderResponse;
    }

}
