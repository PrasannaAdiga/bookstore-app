package com.learning.bookstore.adapter.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Entity
@Audited
@Table(name = "Order_Item")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntity extends Auditor<String> {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private OrderEntity order;

    @Column(name = "product_id", nullable = false)
    @NotBlank(message = "Product Id should not be blank")
    private String productId;

    @Column(name = "quantity", nullable = false)
    @Positive(message = "Quantity should be positive")
    private int quantity;

    @Column(name = "order_item_price", nullable = false)
    @Positive(message = "Order Item Price should be positive")
    private double orderItemPrice;

    @Column(name = "order_extended_price", nullable = false)
    @Positive(message = "Order extended price should be positive")
    private double orderExtendedPrice;

}
