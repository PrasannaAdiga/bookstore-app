package com.learning.bookstore.adapter.persistence.repository;

import com.learning.bookstore.adapter.persistence.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CartItemRepository extends JpaRepository<CartItemEntity, String> {
    @Modifying
    @Query("delete from CartItemEntity c where c.cart.id = ?1")
    void deleteByCartId(String cartId);

}
