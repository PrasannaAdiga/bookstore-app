package com.bookstore.learning.adapter.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Audited
@Table(name = "User_Payment_Customer")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPaymentCustomerEntity extends Auditor<String> {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(name = "user_id", nullable = false, unique = true)
    @NotBlank(message = "User Email should not be blank")
    private String userEmail;

    @Column(name = "payment_customer_id", nullable = false, unique = true)
    @NotBlank(message = "Payment Customer Id should not be blank")
    private String paymentCustomerId;
}
