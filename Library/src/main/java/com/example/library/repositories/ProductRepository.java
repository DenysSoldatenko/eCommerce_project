package com.example.library.repositories;

import com.example.library.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * Repository interface for accessing and managing Product entities.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
