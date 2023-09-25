package com.example.admin.controllers;

import com.example.admin.validations.CategoryExceptionService;
import com.example.library.models.Category;
import com.example.library.services.CategoryService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller class for handling category-related operations.
 */
@Controller
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;
  private final CategoryExceptionService categoryExceptionService;

  /**
   * Handles the request for displaying categories.
   *
   * @param model     the model to be populated with attributes
   * @return the view name for displaying categories
   *     or a redirect to the login page if not authenticated
   */
  @GetMapping("/categories")
  public String categories(Model model) {
    List<Category> categories = categoryService.findAllCategories();
    model.addAttribute("categories", categories);
    model.addAttribute("size", categories.size());
    model.addAttribute("title", "Category");
    return "categories";
  }

  /**
   * Displays the form for adding a new category.
   *
   * @param model     the model to be populated with attributes
   * @return the view name for adding a new category
   *     or a redirect to the login page if not authenticated
   */
  @GetMapping("/add-category")
  public String addCategoryForm(Model model) {
    model.addAttribute("categoryNew", new Category());
    return "add-category";
  }

  /**
   * Adds a new category.
   *
   * @param category   the category to be added
   * @param attributes the redirect attributes
   * @return a redirect to the category page after adding the category
   */
  @PostMapping("/add-category")
  public String addCategory(@ModelAttribute("categoryNew") Category category,
                           RedirectAttributes attributes) {
    categoryExceptionService.validate(category);

    if (Objects.equals(categoryExceptionService.getErrorMessage(), "")) {
      categoryService.createCategory(category);
      attributes.addFlashAttribute("success", "Added successfully");
    } else {
      attributes.addFlashAttribute("fail", categoryExceptionService.getErrorMessage());
      return "redirect:/add-category";
    }
    return "redirect:/categories";
  }

  /**
   * Displays the form for updating a category.
   *
   * @param id        the ID of the category to update
   * @param model     the model to be populated with attributes
   * @return the view name for updating the category
   *     or a redirect to the login page if not authenticated
   */
  @GetMapping("/update-category/{id}")
  public String updateCategoryForm(@PathVariable("id") Long id, Model model) {
    model.addAttribute("title", "Update categories");
    Category category = categoryService.findCategoryById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Product not found with id: " + id));
    model.addAttribute("category", category);
    return "update-category";
  }

  /**
   * Updates a category.
   *
   * @param category   the updated category
   * @param attributes the redirect attributes
   * @return a redirect to the category page after updating the category
   */
  @PostMapping("/update-category/{id}")
  public String updateCategory(Category category, RedirectAttributes attributes) {
    categoryExceptionService.validate(category);

    if (Objects.equals(categoryExceptionService.getErrorMessage(), "")) {
      categoryService.updateCategory(category);
      attributes.addFlashAttribute("success", "Updated successfully");
    } else {
      attributes.addFlashAttribute("fail", categoryExceptionService.getErrorMessage());
      return "redirect:/update-category/{id}";
    }
    return "redirect:/categories";
  }

  /**
   * Handles the request for deleting a category.
   *
   * @param id         the ID of the category to delete
   * @param attributes the redirect attributes to add flash attributes
   * @return a redirect to the category page after deleting the category
   */
  @RequestMapping(value = "/delete-category/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
  public String deleteCategory(@PathVariable Long id, RedirectAttributes attributes) {
    categoryService.deleteCategoryById(id);
    attributes.addFlashAttribute("success", "Deleted successfully");
    return "redirect:/categories";
  }

  /**
   * Handles the request for enabling a category.
   *
   * @param id         the ID of the category to enable
   * @param attributes the redirect attributes to add flash attributes
   * @return a redirect to the category page after enabling the category
   */
  @RequestMapping(value = "/enable-category/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
  public String enableCategory(@PathVariable Long id, RedirectAttributes attributes) {
    categoryService.enableCategoryById(id);
    attributes.addFlashAttribute("success", "Enabled successfully");
    return "redirect:/categories";
  }
}