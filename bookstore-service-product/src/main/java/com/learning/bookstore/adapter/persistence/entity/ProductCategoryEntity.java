package com.learning.bookstore.adapter.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Audited
@Table(name = "Product_Category")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryEntity extends Auditor<String> {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column
    @NotBlank(message = "Product category name should not be blank")
    private String name;

    @Column
    @Length(min = 5, max = 100, message = "Product category description should be 5 to 100 characters")
    private String description;

    @OneToMany(mappedBy = "productCategoryEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductEntity> productEntities;

}
