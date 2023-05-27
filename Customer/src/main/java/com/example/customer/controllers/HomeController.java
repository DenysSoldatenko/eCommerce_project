package com.example.customer.controllers;

import com.example.library.dtos.ProductDto;
import com.example.library.models.Category;
import com.example.library.services.CategoryService;
import com.example.library.services.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {
  private final ProductService productService;

  private final CategoryService categoryService;

  @Autowired
  public HomeController(ProductService productService, CategoryService categoryService) {
    this.productService = productService;
    this.categoryService = categoryService;
  }

  @GetMapping({"/index", "/"})
  public String home(Principal principal, HttpSession session) {
    if (principal != null) {
      session.setAttribute("username", principal.getName());
    } else {
      session.removeAttribute("username");
      return "redirect:/login";
    }
    return "home";
  }

  @GetMapping("/home")
  public String index(Model model) {
    List<Category> categoryList = categoryService.findAll();
    List<ProductDto> productDtoList = productService.findAll();
    model.addAttribute("categories", categoryList);
    model.addAttribute("products", productDtoList);
    return "index";
  }
}