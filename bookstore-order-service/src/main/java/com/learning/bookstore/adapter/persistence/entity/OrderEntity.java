package com.learning.bookstore.adapter.persistence.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Audited
@Table(name = "User_Order")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity extends Auditor<String> {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(name = "user_mail", nullable = false)
    @NotBlank(message = "User Email should not be blank")
    private String userMail;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@NotEmpty(message = "Order items should not be empty")
    private List<OrderItemEntity> orderItems;

    @Column(name = "total_item_price", nullable = false)
    @Positive(message = "Total items price should be positive")
    private double totalItemsPrice;

    @Column(name = "total_order_price", nullable = false)
    @Positive(message = "Total order price should be positive")
    private double totalOrderPrice;

    @Column(name = "payment_method_id", nullable = false)
    @NotBlank(message = "Payment method id should not blank")
    private String paymentMethodId;

    @Column(name = "billing_address_id", nullable = false)
    @NotBlank(message = "Billing address id should not blank")
    private String billingAddressId;

    @Column(name = "shipping_address_id", nullable = false)
    @NotBlank(message = "Shipping address id should not blank")
    private String shippingAddressId;

    @Column(name = "tax_price", nullable = false)
    @Positive(message = "Tax price should be positive")
    private double taxPrice;

    @Column(name = "shipping_price", nullable = false)
    @Positive(message = "Shipping price should be positive")
    private double shippingPrice;

    @Column(name = "is_paid")
    private boolean isPaid;

    @Column(name = "payment_id")
    @NotBlank(message = "payment method id should not be blank")
    private String paymentId;

    @Column(name = "payment_date")
    @NotNull(message = "Payment Date should not be null")
    private LocalDateTime paymentDate;

    @Column(name = "payment_receipt_url")
    @NotBlank(message = "Payment Receipt URL should not be blank")
    private String paymentReceiptUrl;

    @Column(name = "delivered_date")
    @NotNull(message = "Delivered Date should not be null")
    private LocalDateTime deliveredDate;

    @Column(name = "is_delivered")
    private boolean isDelivered;

}
