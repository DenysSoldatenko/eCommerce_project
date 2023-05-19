package com.example.library.repositories;

import com.example.library.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing CartItem entities.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
