package com.learning.bookstore.adapter.persistence.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Entity
@Audited
@Table(name = "Cart")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity extends Auditor<String> {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(name = "user_email", nullable = false, unique = true)
    @NotBlank(message = "User Email should not be blank")
    private String userEmail;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItemEntity> cartItems;

    @Column(name = "total_price", nullable = false)
    @PositiveOrZero(message = "Total price should be zero or positive")
    private double totalPrice;

}
