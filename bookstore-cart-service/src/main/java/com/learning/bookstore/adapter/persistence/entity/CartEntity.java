package com.learning.bookstore.adapter.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Entity
@Audited
@Table(name = "Cart")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity extends Auditor<String> {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    @NotBlank(message = "Cart Id should not be blank")
    private String id;

    @Column(name = "user_email", nullable = false, unique = true)
    @NotBlank(message = "User Email should not be blank")
    private String userEmail;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItemEntity> cartItems;

    @Column(name = "total_price", nullable = false)
    @Positive(message = "Total price should be positive")
    private double totalPrice;

}
