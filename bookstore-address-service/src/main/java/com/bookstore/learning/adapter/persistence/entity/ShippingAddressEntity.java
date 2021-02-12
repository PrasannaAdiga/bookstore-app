package com.bookstore.learning.adapter.persistence.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Audited
@Table(name = "Shipping_Address")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddressEntity extends Auditor<String> {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    @NotBlank(message = "Shipping Address Id should not be blank")
    private String id;

    @Column(name = "user_email", nullable = false, unique = true)
    @NotBlank(message = "User Email should not be blank")
    private String userEmail;

    @Column(name = "address_line1", nullable = false)
    @NotBlank(message = "Address line1 should not be blank")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column
    @NotBlank(message = "City should not be blank")
    private String city;

    @Column
    @NotBlank(message = "State should not be blank")
    private String state;

    @Column(name = "postal_code")
    @NotBlank(message = "Postal Code should not be blank")
    private String postalCode;

    @Column
    @NotBlank(message = "Country should not be blank")
    @Pattern(regexp = "[A-Z]{2}", message = "2-letter ISO country code required")
    private String country;

    @Column
    @NotBlank(message = "Phone should not be blank")
    private String phone;

}
