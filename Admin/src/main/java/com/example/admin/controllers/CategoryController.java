package com.example.admin.controllers;

import com.example.admin.utils.CategoryExceptionManager;
import com.example.library.models.Category;
import com.example.library.services.CategoryService;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller class for handling category-related operations.
 */
@Controller
public class CategoryController {

  private final CategoryService categoryService;
  private final CategoryExceptionManager categoryExceptionManager;

  @Autowired
  public CategoryController(CategoryService categoryService,
                            CategoryExceptionManager categoryExceptionManager) {
    this.categoryService = categoryService;
    this.categoryExceptionManager = categoryExceptionManager;
  }

  /**
   * Handles the request for displaying categories.
   *
   * @param model     the model to be populated with attributes
   * @param principal the principal object representing the authenticated user
   * @return the view name for displaying categories
   *     or a redirect to the login page if not authenticated
   */
  @GetMapping("/categories")
  public String categories(Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    List<Category> categories = categoryService.findAll();
    model.addAttribute("categories", categories);
    model.addAttribute("size", categories.size());
    model.addAttribute("title", "Category");
    model.addAttribute("categoryNew", new Category());
    return "categories";
  }

  /**
   * Handles the request for adding a new category.
   *
   * @param category  the Category object to be added
   * @param attributes the redirect attributes to add flash attributes
   * @return a redirect to the category page after adding the category
   */
  @PostMapping("/add-category")
  public String add(@ModelAttribute("categoryNew") Category category,
                    RedirectAttributes attributes) {
    categoryExceptionManager.validate(category);

    if (Objects.equals(categoryExceptionManager.getMessage(), "")) {
      categoryService.save(category);
      attributes.addFlashAttribute("success", "Added successfully");
    } else {
      attributes.addFlashAttribute("failed", categoryExceptionManager.getMessage());
    }
    return "redirect:/categories";
  }

  @RequestMapping(value = "/findById/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
  @ResponseBody
  public Category findById(@PathVariable Long id) {
    Optional<Category> categoryOptional = categoryService.findById(id);
    return categoryOptional.orElse(null);
  }

  /**
   * Handles the request for updating a category.
   *
   * @param category   the updated Category object
   * @param attributes the redirect attributes to add flash attributes
   * @return a redirect to the category page after updating the category
   */
  @GetMapping("/update-category")
  public String update(Category category, RedirectAttributes attributes) {
    categoryExceptionManager.validate(category);

    if (Objects.equals(categoryExceptionManager.getMessage(), "")) {
      categoryService.update(category);
      attributes.addFlashAttribute("success", "Updated successfully");
    } else {
      attributes.addFlashAttribute("failed", categoryExceptionManager.getMessage());
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
  public String delete(@PathVariable Long id, RedirectAttributes attributes) {
    try {
      categoryService.deleteById(id);
      attributes.addFlashAttribute("success", "Deleted successfully");
    } catch (Exception e) {
      e.printStackTrace();
      attributes.addFlashAttribute("failed", "Failed to deleted");
    }
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
  public String enable(@PathVariable Long id, RedirectAttributes attributes) {
    try {
      categoryService.enabledById(id);
      attributes.addFlashAttribute("success", "Enabled successfully");
    } catch (Exception e) {
      e.printStackTrace();
      attributes.addFlashAttribute("failed", "Failed to enabled");
    }
    return "redirect:/categories";
  }
}