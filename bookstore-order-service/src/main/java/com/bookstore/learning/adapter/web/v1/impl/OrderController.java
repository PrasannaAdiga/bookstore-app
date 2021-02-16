package com.bookstore.learning.adapter.web.v1.impl;

import com.bookstore.learning.adapter.web.v1.IOrderController;
import com.bookstore.learning.application.port.in.IOrderCommandService;
import com.bookstore.learning.application.port.in.IOrderQueryService;
import com.bookstore.learning.application.service.request.CreateOrderRequest;
import com.bookstore.learning.application.service.request.PreviewOrderRequest;
import com.bookstore.learning.application.service.response.OrderResponse;
import com.bookstore.learning.application.service.response.PreviewOrderResponse;
import com.bookstore.learning.infrastructure.annotation.WebAdapter;
import com.bookstore.learning.infrastructure.constant.OrderServiceConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@WebAdapter
@RequiredArgsConstructor
public class OrderController implements IOrderController {
    private final IOrderCommandService orderCommandService;
    private final IOrderQueryService orderQueryService;

    /*============================================ Command Section ===============================================*/

    @Override
    public ResponseEntity<OrderResponse> createOrder(CreateOrderRequest createOrderRequest,
                                                     @AuthenticationPrincipal Jwt principal) {
        String loggedInUserEmail = principal.getClaimAsString(OrderServiceConstant.ACCESS_TOKEN_EMAIL_FIELD_NAME);
        return ResponseEntity.ok().body(orderCommandService.createOrderForLoggedInUser(createOrderRequest, loggedInUserEmail));
    }

    @Override
    public ResponseEntity<PreviewOrderResponse> previewOrder(PreviewOrderRequest previewOrderRequest,
                                                             @AuthenticationPrincipal Jwt principal) {
        String loggedInUserEmail = principal.getClaimAsString(OrderServiceConstant.ACCESS_TOKEN_EMAIL_FIELD_NAME);
        return ResponseEntity.ok().body(orderCommandService.previewOrderOfLoggedInUser(previewOrderRequest, loggedInUserEmail));
    }

    /*============================================ Query Section ================================================*/

    @Override
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok().body(orderQueryService.getAllPlacedOrder());
    }

    @Override
    public ResponseEntity<OrderResponse> getOrderById(String orderId, @AuthenticationPrincipal Jwt principal) {
        String loggedInUserEmail = principal.getClaimAsString(OrderServiceConstant.ACCESS_TOKEN_EMAIL_FIELD_NAME);
        return ResponseEntity.ok().body(orderQueryService.getOrderOfUserById(orderId, loggedInUserEmail));
    }

    @Override
    public ResponseEntity<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal Jwt principal) {
        String loggedInUserEmail = principal.getClaimAsString(OrderServiceConstant.ACCESS_TOKEN_EMAIL_FIELD_NAME);
        return ResponseEntity.ok().body(orderQueryService.getAllOrderOfUser(loggedInUserEmail));
    }

}
