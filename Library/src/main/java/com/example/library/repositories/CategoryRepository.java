package com.example.library.repositories;

import com.example.library.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * Repository interface for accessing and managing Category entities.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
