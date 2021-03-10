package com.learning.bookstore.adapter.persistence.repository;

import com.learning.bookstore.adapter.persistence.entity.ProductReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ProductReviewRepository extends JpaRepository<ProductReviewEntity, String> {
    @Query("select p from ProductReviewEntity p where p.productEntity.id = ?1")
    Page<ProductReviewEntity> getAllReviewOfProductByPage(String productId, Pageable pageable);

    @Query("select p.rating from ProductReviewEntity p where p.productEntity.id = ?1")
    List<Integer> getAllReviewRatingOfProduct(String productId);

}
