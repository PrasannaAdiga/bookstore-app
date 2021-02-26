package com.learning.bookstore.adapter.web.v1.impl;

import com.learning.bookstore.adapter.web.v1.IOrderController;
import com.learning.bookstore.application.port.in.IOrderCommandService;
import com.learning.bookstore.application.port.in.IOrderQueryService;
import com.learning.bookstore.application.service.request.CreateOrderRequest;
import com.learning.bookstore.application.service.request.PreviewOrderRequest;
import com.learning.bookstore.application.service.response.OrderResponse;
import com.learning.bookstore.application.service.response.PreviewOrderResponse;
import com.learning.bookstore.infrastructure.annotation.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<OrderResponse> createOrder(CreateOrderRequest createOrderRequest) {
        return ResponseEntity.ok().body(orderCommandService.createOrderForLoggedInUser(createOrderRequest));
    }

    @Override
    public ResponseEntity<PreviewOrderResponse> previewOrder(PreviewOrderRequest previewOrderRequest) {
        return ResponseEntity.ok().body(orderCommandService.previewOrderOfLoggedInUser(previewOrderRequest));
    }

    /*============================================ Query Section ================================================*/

    @Override
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok().body(orderQueryService.getAllPlacedOrder());
    }

    @Override
    public ResponseEntity<OrderResponse> getOrderById(String orderId) {
        return ResponseEntity.ok().body(orderQueryService.getOrderOfUserById(orderId));
    }

    @Override
    public ResponseEntity<List<OrderResponse>> getMyOrders() {
        return ResponseEntity.ok().body(orderQueryService.getAllOrderOfUser());
    }

}
