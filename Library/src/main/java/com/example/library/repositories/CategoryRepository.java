package com.example.library.repositories;

import com.example.library.dtos.CategoryDto;
import com.example.library.models.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing Category entities.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByName(String name);

  @Query("SELECT c FROM Category c WHERE c.isActivated = true AND c.isDeleted = false")
  List<Category> findAllByActivated();

  /*Customer*/
  @Query("SELECT new com.example.library.dtos.CategoryDto(c.id, c.name, COUNT(p.category.id)) "
      + "FROM Category c "
      + "INNER JOIN Product p ON p.category.id = c.id "
      + "WHERE c.isActivated = true AND c.isDeleted = false "
      + "GROUP BY c.id")
  List<CategoryDto> getCategoriesWithProductCounts();
}
