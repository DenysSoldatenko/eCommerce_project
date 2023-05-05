package com.example.library.services;

import com.example.library.models.Category;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing categories.
 */
public interface CategoryService {
  List<Category> findAll();

  Category save(Category category);

  Optional<Category> findById(Long id);

  Optional<Category> findByName(String name);

  void update(Category category);

  void deleteById(Long id);

  void enabledById(Long id);
}