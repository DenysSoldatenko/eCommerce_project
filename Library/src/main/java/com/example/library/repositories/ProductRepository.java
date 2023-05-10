package com.example.library.repositories;

import com.example.library.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing Product entities.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  @Query("select p from Product p")
  Page<Product> pageProduct(Pageable pageable);

  @Query("select p from Product p where p.description like %?1% or p.name like %?1%")
  Page<Product> searchProducts(String keyword, Pageable pageable);
}