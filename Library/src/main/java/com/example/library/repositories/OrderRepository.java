package com.example.library.repositories;

import com.example.library.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing Order entities.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
