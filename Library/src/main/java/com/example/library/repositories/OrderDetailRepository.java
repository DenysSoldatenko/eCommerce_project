package com.example.library.repositories;

import com.example.library.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing OrderDetail entities.
 */
@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
