package com.example.admin.validations.impl;

import com.example.admin.validations.CategoryExceptionService;
import com.example.library.models.Category;
import com.example.library.services.CategoryService;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Utility class for handling exceptions related to the Category.
 */
@Component
@RequiredArgsConstructor
public class CategoryExceptionServiceImpl implements CategoryExceptionService {

  private final CategoryService categoryService;

  @Getter
  private String errorMessage;

  /**
   * Validates the given Category object for any potential exceptions.
   *
   * @param category the Category object to be validated
   */
  @Override
  public void validate(Category category) {
    errorMessage = "";
    handleEmptyName(category);
    handleDuplicateName(category);
  }

  @Override
  public void handleEmptyName(Category category) {
    if (category.getName() == null || category.getName().equals("")) {
      errorMessage = "Failed to perform because of a empty name";
    }
  }

  @Override
  public void handleDuplicateName(Category category) {
    Optional<Category> cat = categoryService.findCategoryByName(category.getName());
    if (cat.isPresent()) {
      errorMessage = "Failed to perform because of a duplicate name";
    }
  }
}
