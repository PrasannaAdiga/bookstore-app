package com.bookstore.learning.application.service;

import com.bookstore.learning.adapter.client.AddressFeignClient;
import com.bookstore.learning.adapter.client.CartFeignClient;
import com.bookstore.learning.adapter.client.PaymentFeignClient;
import com.bookstore.learning.adapter.client.dto.*;
import com.bookstore.learning.application.exception.EmptyCartException;
import com.bookstore.learning.application.exception.InvalidPaymentMethodException;
import com.bookstore.learning.application.port.in.IOrderCommandService;
import com.bookstore.learning.application.port.out.IOrderDataProvider;
import com.bookstore.learning.application.service.request.CreateOrderRequest;
import com.bookstore.learning.application.service.request.PreviewOrderRequest;
import com.bookstore.learning.application.service.response.CardResponse;
import com.bookstore.learning.application.service.response.OrderResponse;
import com.bookstore.learning.application.service.response.OrderItemResponse;
import com.bookstore.learning.application.service.response.PreviewOrderResponse;
import com.bookstore.learning.domain.Order;
import com.bookstore.learning.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class OrderCommandService implements IOrderCommandService {
    private final IOrderDataProvider orderDataProvider;
    private final AddressFeignClient addressFeignClient;
    private final CartFeignClient cartFeignClient;
    private final PaymentFeignClient paymentFeignClient;

    @Override
    public OrderResponse createOrderForLoggedInUser(CreateOrderRequest request, String loggedInUserEmail) {
        OrderResponse orderResponse = new OrderResponse();

        //Get billing address of logged in user
        BillingAddressResponse billingAddressResponse = null;
        if(request.getBillingAddressId() != null && !request.getBillingAddressId().isEmpty()) {
            billingAddressResponse = addressFeignClient.getBillingAddressById(request.getBillingAddressId());
            orderResponse.setBillingAddress(billingAddressResponse);
        }

        //Get shipping address of logged in user
        ShippingAddressResponse shippingAddressResponse = null;
        if(request.getShippingAddressId() != null && !request.getShippingAddressId().isEmpty()) {
            shippingAddressResponse = addressFeignClient.getShippingAddressById(request.getShippingAddressId());
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
        CartResponse cartResponse = cartFeignClient.getCart();
        if(cartResponse.getCartItems().isEmpty()) {
            log.error("EmptyCartException in OrderCommandService.createOrderForLoggedInUser: No Cart Item found for the user email {}", loggedInUserEmail);
            throw  new EmptyCartException("EmptyCartException in OrderCommandService.createOrderForLoggedInUser: No Cart Item found for the user email " + loggedInUserEmail);
        }

        //Create Order and Order Items
        Order order = new Order();
        order.setUserEmail(loggedInUserEmail);
        cartResponse.getCartItems().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setOrderItemPrice(cartItem.getPrice());
            orderItem.setOrderExtendedPrice(cartItem.getExtendedPrice());
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            order.getOrderItems().add(orderItem);
            OrderItemResponse orderItemResponse = OrderItemResponse.builder()
                    .id(orderItem.getId())
                    .orderItemPrice(orderItem.getOrderItemPrice())
                    .orderExtendedPrice(orderItem.getOrderExtendedPrice())
                    .order(orderItem.getOrder())
                    .productId(orderItem.getProductId())
                    .quantity(orderItem.getQuantity())
                    .build();
            orderResponse.getOrderItems().add(orderItemResponse);
        });

        //Set totalItemPrice of Order
        double itemsPrice = orderResponse.getOrderItems().stream().mapToDouble(OrderItemResponse::getOrderExtendedPrice).sum();
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
                .currency("USD")
                .paymentMethodId(request.getPaymentMethodId())
                .build();
        CreatePaymentResponse createPaymentResponse = paymentFeignClient.doPayment(createPaymentRequest);

        //Update order with payment details
        order.setPaid(createPaymentResponse.isCaptured());
        order.setPaymentDate(createPaymentResponse.getPaymentDate());
        order.setPaymentId(createPaymentResponse.getPaymentId());
        order.setPaymentReceiptUrl(createPaymentResponse.getReceipt_url());
        order.setPaymentMethodId(request.getPaymentMethodId());

        //Save the order in DB
        Order savedOrder = orderDataProvider.createOrder(order);

        //Update createOrderResponse
        orderResponse.setOrderId(savedOrder.getId());
        orderResponse.setCreated_at(savedOrder.getCreationDate());
        orderResponse.setPaid(createPaymentResponse.isCaptured());
        orderResponse.setPaymentDate(createPaymentResponse.getPaymentDate());
        orderResponse.setPaymentReceiptUrl(createPaymentResponse.getReceipt_url());

        //Clear cart
        cartFeignClient.removeAllCartItem(cartResponse.getId());
        return orderResponse;
    }

    @Override
    public PreviewOrderResponse previewOrderOfLoggedInUser(PreviewOrderRequest request, String loggedInUserEmail) {
        PreviewOrderResponse previewOrderResponse = new PreviewOrderResponse();

        //Get billing address of logged in user
        BillingAddressResponse billingAddressResponse = null;
        if(request.getBillingAddressId() != null && !request.getBillingAddressId().isEmpty()) {
            billingAddressResponse = addressFeignClient.getBillingAddressById(request.getBillingAddressId());
            previewOrderResponse.setBillingAddress(billingAddressResponse);
        }

        //Get shipping address of logged in user
        ShippingAddressResponse shippingAddressResponse = null;
        if(request.getShippingAddressId() != null && !request.getShippingAddressId().isEmpty()) {
            shippingAddressResponse = addressFeignClient.getShippingAddressById(request.getShippingAddressId());
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
            PaymentMethodResponse paymentMethodResponse = paymentFeignClient.getPaymentMethodOfUserById(request.getPaymentMethodId());
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

        //Get Cart details
        CartResponse cartResponse = cartFeignClient.getCart();
        if(cartResponse.getCartItems().isEmpty()) {
            log.error("EmptyCartException in OrderCommandService.createOrderForLoggedInUser: No Cart Item found for the user email {}", loggedInUserEmail);
            throw  new EmptyCartException("EmptyCartException in OrderCommandService.createOrderForLoggedInUser: No Cart Item found for the user email " + loggedInUserEmail);
        }

        //Set order items
        cartResponse.getCartItems()
                .forEach(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderItemPrice(cartItem.getPrice());
                    orderItem.setOrderExtendedPrice(cartItem.getExtendedPrice());
                    orderItem.setProductId(cartItem.getProductId());
                    orderItem.setQuantity(cartItem.getQuantity());
                    previewOrderResponse.getOrderItems().add(OrderItemResponse.builder()
                            .order(orderItem.getOrder())
                            .orderExtendedPrice(orderItem.getOrderExtendedPrice())
                            .orderItemPrice(orderItem.getOrderItemPrice())
                            .productId(orderItem.getProductId())
                            .quantity(orderItem.getQuantity())
                            .id(orderItem.getId())
                            .build());
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
