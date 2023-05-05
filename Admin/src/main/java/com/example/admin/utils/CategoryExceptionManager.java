package com.example.admin.utils;

import com.example.library.models.Category;
import com.example.library.services.CategoryService;
import java.util.Optional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Utility class for handling exceptions related to the Category.
 */
@Component
@Getter
public class CategoryExceptionManager {
  private final CategoryService categoryService;
  private String message;

  @Autowired
  public CategoryExceptionManager(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  /**
   * Validates the given Category object for any potential exceptions.
   *
   * @param category the Category object to be validated
   */
  public void validate(Category category) {
    message = "";
    handleEmptyName(category);
    handleDuplicateName(category);
  }

  private void handleEmptyName(Category category) {
    if (category.getName() == null || category.getName().equals("")) {
      message = "Failed to perform because empty name";
    }
  }

  private void handleDuplicateName(Category category) {
    Optional<Category> cat = categoryService.findByName(category.getName());
    if (cat.isPresent()) {
      message = "Failed to perform because duplicate name";
    }
  }
}
