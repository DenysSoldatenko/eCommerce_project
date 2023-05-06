package com.example.library.repositories;

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

  @Query("select c from Category c where c.isActivated = true and c.isDeleted = false")
  List<Category> findAllByActivated();
}
