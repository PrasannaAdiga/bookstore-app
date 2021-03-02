package com.learning.bookstore.adapter.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Audited
@Table(name = "Cart_Item")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemEntity extends Auditor<String> {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column()
    private double price;

    @Column(name = "extended_price")
    private double extendedPrice;

    @Column()
    private int quantity;

    @Column(name = "product_id")
    @NotBlank(message = "ProductId should not be blank")
    private String productId;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private CartEntity cart;

}
