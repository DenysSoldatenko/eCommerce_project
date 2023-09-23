package com.example.library.services.implementations;

import com.example.library.dtos.CategoryDto;
import com.example.library.models.Category;
import com.example.library.models.Product;
import com.example.library.repositories.CategoryRepository;
import com.example.library.services.CategoryService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the CategoryService interface.
 */
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;

  @Override
  public List<Category> findAllCategories() {
    return categoryRepository.findAll();
  }

  @Override
  public void createCategory(Category category) {
    Category categorySave = new Category(category.getName());
    categoryRepository.save(categorySave);
  }

  @Override
  public Optional<Category> findCategoryById(Long id) {
    return categoryRepository.findById(id);
  }

  @Override
  public Optional<Category> findCategoryByName(String name) {
    return categoryRepository.findByName(name);
  }

  @Override
  public void updateCategory(Category category) {
    Optional<Category> categoryToUpdate = categoryRepository.findById(category.getId());
    categoryToUpdate.ifPresent(existingCategory -> {
      existingCategory.setName(category.getName());
      existingCategory.setActivated(category.isActivated());
      existingCategory.setDeleted(category.isDeleted());
      categoryRepository.save(existingCategory);
    });
  }

  @Override
  public void deleteCategoryById(Long id) {
    Optional<Category> category = categoryRepository.findById(id);
    category.ifPresent(existingCategory -> {
      existingCategory.setDeleted(true);
      existingCategory.setActivated(false);
      categoryRepository.save(existingCategory);
    });
  }

  @Override
  public void enableCategoryById(Long id) {
    Optional<Category> category = categoryRepository.findById(id);
    category.ifPresent(existingCategory -> {
      existingCategory.setDeleted(false);
      existingCategory.setActivated(true);
      categoryRepository.save(existingCategory);
    });
  }

  @Override
  public List<Category> findActivatedCategories() {
    return categoryRepository.findAllByActivated();
  }

  @Override
  public List<CategoryDto> getCategoryAndProductCounts() {
    return categoryRepository.getCategoriesWithProductCounts();
  }

  @Override
  public List<Category> findFilteredCategoriesByProducts(List<Product> filteredProducts) {
    return categoryRepository.findAllByActivated().stream()
    .filter(category -> filteredProducts.stream()
    .anyMatch(product -> Objects.equals(product.getCategory().getId(), category.getId())))
    .toList();
  }
}