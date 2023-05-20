package com.example.library.repositories;

import com.example.library.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing and managing Product entities.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  @Query("select p from Product p")
  Page<Product> findAllProducts(Pageable pageable);

  @Query("select p from Product p where p.description like %?1% or p.name like %?1%")
  Page<Product> findAllByNameOrDescription(String keyword, Pageable pageable);

  @Query("select p from Product p where p.isDeleted = false and p.isActivated = true")
  List<Product> getAllProducts();

  @Query(value = "select p.product_id, p.name, p.description, p.current_quantity, " +
  "p.cost_price, p.category_id, p.sale_price, p.image, p.is_activated, p.is_deleted " +
  "from products p where p.is_deleted = false and p.is_activated = true limit 4",
  nativeQuery = true)
  List<Product> listViewProduct();
}
