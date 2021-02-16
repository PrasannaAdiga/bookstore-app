package com.bookstore.learning.domain;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends Audit {
    private String id;

    @NotBlank(message = "User Email should not be blank")
    private String userEmail;

    @NotEmpty(message = "Order items should not be empty")
    private List<OrderItem> orderItems;

    @Positive(message = "Total items price should be positive")
    private double totalItemsPrice;

    @Positive(message = "Total order price should be positive")
    private double totalOrderPrice;

    @NotBlank(message = "Payment method id should not blank")
    private String paymentMethodId;

    @NotBlank(message = "Billing address id should not blank")
    private String billingAddressId;

    @NotBlank(message = "Shipping address id should not blank")
    private String shippingAddressId;

    @Positive(message = "Tax price should be positive")
    private double taxPrice;

    @Positive(message = "Shipping price should be positive")
    private double shippingPrice;

    private boolean isPaid;

    @NotBlank(message = "payment method id should not be blank")
    private String paymentId;

    @NotNull(message = "Payment Date should not be null")
    private LocalDateTime paymentDate;

    @NotBlank(message = "Payment Receipt URL should not be blank")
    private String paymentReceiptUrl;

    @NotNull(message = "Delivered Date should not be null")
    private LocalDateTime deliveredDate;

    private boolean isDelivered;

}
