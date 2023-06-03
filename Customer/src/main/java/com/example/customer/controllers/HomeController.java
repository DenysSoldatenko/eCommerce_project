package com.example.customer.controllers;

import com.example.library.dtos.ProductDto;
import com.example.library.models.Category;
import com.example.library.models.Customer;
import com.example.library.models.ShoppingCart;
import com.example.library.services.CategoryService;
import com.example.library.services.CustomerService;
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

  private final CustomerService customerService;

  @Autowired
  public HomeController(ProductService productService, CategoryService categoryService, CustomerService customerService) {
    this.productService = productService;
    this.categoryService = categoryService;
    this.customerService = customerService;
  }

  @GetMapping({"/index", "/"})
  public String home(Principal principal, HttpSession session) {
    if (principal != null) {
      Customer customer = customerService.findByUsername(principal.getName());
      session.setAttribute("username", customer.getFirstName());
      ShoppingCart shoppingCart = customer.getCart();
      if (shoppingCart != null) {
        session.setAttribute("totalItems", shoppingCart.getTotalItems());
      }
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