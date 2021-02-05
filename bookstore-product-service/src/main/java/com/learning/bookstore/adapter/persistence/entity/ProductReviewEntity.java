package com.learning.bookstore.adapter.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Audited
@Table(name = "Product_Review")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewEntity extends Auditor<String> {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(name = "user_id", nullable = false)
    @NotBlank(message = "UserId should not be blank")
    private String userId;

    @Column(nullable = false)
    @Range(min = 1, max = 5, message = "product rating must be between 1 to 5")
    private int rating;

    @Column
    @NotBlank(message = "Review message should not be blank")
    @Length(min = 5, max = 250, message = "Review message should be 5 to 250 characters")
    private String message;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull(message = "Product should not be null")
    private ProductEntity productEntity;

}
