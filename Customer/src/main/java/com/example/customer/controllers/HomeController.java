package com.example.customer.controllers;

import com.example.customer.validations.SessionAttributeSetter;
import com.example.library.dtos.ProductDto;
import com.example.library.models.Category;
import com.example.library.services.CategoryService;
import com.example.library.services.ProductService;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling home-related requests and displaying the home page.
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

  private final ProductService productService;
  private final CategoryService categoryService;
  private final SessionAttributeSetter sessionAttributeSetter;

  @GetMapping({"/index", "/"})
  public String home(Principal principal, HttpSession session) {
    sessionAttributeSetter.setSessionAttributes(principal, session);
    return "home";
  }

  /**
   * Displays the page with categories and products.
   *
   * @param model the model to be populated with attributes
   * @return the name of the index page view
   */
  @GetMapping("/home")
  public String index(Principal principal, HttpSession session, Model model) {
    sessionAttributeSetter.setSessionAttributes(principal, session);
    List<Category> categoryList = categoryService.findAllCategories();
    List<ProductDto> productDtoList = productService.findAll();
    model.addAttribute("categories", categoryList);
    model.addAttribute("products", productDtoList);
    return "index";
  }
}