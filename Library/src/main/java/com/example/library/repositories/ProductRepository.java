package com.example.library.repositories;

import com.example.library.models.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing Product entities.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  @Query("SELECT p FROM Product p")
  Page<Product> findAllProductsPaginated(Pageable pageable);

  @Query("SELECT p FROM Product p WHERE p.description LIKE %:keyword% OR p.name LIKE %:keyword%")
  Page<Product> findAllByNameOrDescriptionPaginated(@Param("keyword") String keyword,
                                                    Pageable pageable);

  /*Customer*/
  @Query("SELECT p FROM Product p "
      + "WHERE p.isActivated = true AND p.isDeleted = false and p.currentQuantity > 0")
  List<Product> findActivatedAndNotDeletedProducts();

  @Query(value = "SELECT p FROM Product p "
      + "WHERE p.isDeleted = false AND p.isActivated = true ")
  Page<Product> findAllViewableProductsPaginated(Pageable pageable);

  @Query(nativeQuery = true,
      value = "SELECT p.* FROM products p "
      + "JOIN categories c ON c.category_id = p.category_id "
      + "WHERE p.category_id = :categoryId")
  List<Product> findProductsByCategoryId(@Param("categoryId") Long categoryId);

  @Query("SELECT p FROM Product p "
      + "JOIN p.category c "
      + "WHERE c.id = :categoryId AND p.isDeleted = false AND p.isActivated = true")
  List<Product> getProductsInCategory(@Param("categoryId") Long categoryId);

  @Query("SELECT p FROM Product p WHERE p.isActivated = true AND p.isDeleted = false"
      + " ORDER BY p.costPrice DESC")
  List<Product> filterProductsByHighPrice();

  @Query("SELECT p FROM Product p WHERE p.isActivated = true AND p.isDeleted = false"
      + " ORDER BY p.costPrice")
  List<Product> filterProductsByLowPrice();

  @Query("SELECT p FROM Product p WHERE p.isActivated = true AND p.name LIKE %:keyword% "
      + "OR p.description LIKE %:keyword%")
  List<Product> searchProductsByNameOrDescription(@Param("keyword") String keyword);
}
