package com.example.admin.validations;

import com.example.library.models.Category;

/**
 * Service for handling exceptions and validations related to Category objects.
 */
public interface CategoryExceptionService {

  void validate(Category category);

  void handleEmptyName(Category category);

  void handleDuplicateName(Category category);

  String getErrorMessage();
}
