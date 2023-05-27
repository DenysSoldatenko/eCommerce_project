package com.example.library.repositories;

import com.example.library.models.Product;
import java.util.List;
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
  @Query("SELECT p FROM Product p")
  Page<Product> findAllProducts(Pageable pageable);

  @Query("SELECT p FROM Product p "
      + "WHERE p.description "
      + "LIKE %?1% OR p.name LIKE %?1%")
  Page<Product> findAllByNameOrDescription(String keyword, Pageable pageable);

  /*Customer*/
  @Query("SELECT p FROM Product p "
      + "WHERE p.isActivated = true AND p.isDeleted = false")
  List<Product> getAllProducts();

  @Query(value = "SELECT p FROM Product p "
      + "WHERE p.isDeleted = false AND p.isActivated = true ")
  Page<Product> listViewProducts(Pageable pageable);

  @Query(value = "SELECT p.* FROM products p "
      + "JOIN categories c ON c.category_id = p.category_id "
      + "WHERE p.category_id = ?1", nativeQuery = true)
  List<Product> getRelatedProducts(Long categoryId);

  @Query("SELECT p FROM Product p "
      + "JOIN Category c ON c.id = p.category.id "
      + "WHERE c.id = ?1 AND p.isDeleted = false AND p.isActivated = true")
  List<Product> getProductsInCategory(Long categoryId);

  @Query("SELECT p FROM Product p WHERE p.isActivated = true AND p.isDeleted = false"
      + " ORDER BY p.costPrice DESC")
  List<Product> filterHighPrice();

  @Query("SELECT p FROM Product p WHERE p.isActivated = true AND p.isDeleted = false"
      + " ORDER BY p.costPrice")
  List<Product> filterLowPrice();

}
