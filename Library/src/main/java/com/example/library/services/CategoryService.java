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
  List<Category> findAllCategories();

  void createCategory(Category category);

  Optional<Category> findCategoryById(Long id);

  Optional<Category> findCategoryByName(String name);

  void updateCategory(Category category);

  void deleteCategoryById(Long id);

  void enableCategoryById(Long id);

  List<Category> findActivatedCategories();

  /*Customer*/
  List<CategoryDto> getCategoryAndProductCounts();

  List<Category> findFilteredCategoriesByProducts(List<Product> filteredProducts);
}