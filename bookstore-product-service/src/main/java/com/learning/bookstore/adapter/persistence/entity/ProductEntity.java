package com.learning.bookstore.adapter.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Audited
@Table(name = "Product")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity extends Auditor<String> {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    @NotBlank(message = "ProductId should not be blank")
    private String id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Product name should not be blank")
    private String name;

    @Column
    @Length(min = 5, max = 100, message = "Product description should be 5 to 100 characters")
    private String description;

    @Column
    @Positive(message = "Product price should be positive")
    private double price;

    @Column(name = "image_id")
    private String imageId;

    @ManyToOne
    @JoinColumn(name = "product_category_id")
    @NotNull(message = "Product category should not be blank")
    private ProductCategoryEntity productCategoryEntity;

    @Column(name = "available_count")
    @PositiveOrZero(message = "Available product count should be positive")
    private int availableCount;

    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductReviewEntity> productReviewEntities;

}
