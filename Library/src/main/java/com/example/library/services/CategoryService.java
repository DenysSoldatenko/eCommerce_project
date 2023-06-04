package com.example.library.services;

import com.example.library.dtos.CategoryDto;
import com.example.library.models.Category;
import com.example.library.models.Product;
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

  List<Category> findAllByActivated();

  /*Customer*/
  List<CategoryDto> getCategoryAndProduct();

  List<Category> getFilteredCategories(List<Product> filteredProducts);
}