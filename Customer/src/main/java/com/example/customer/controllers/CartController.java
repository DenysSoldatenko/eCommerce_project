package com.example.customer.controllers;

import com.example.library.models.Customer;
import com.example.library.models.Product;
import com.example.library.models.ShoppingCart;
import com.example.library.services.CustomerService;
import com.example.library.services.ProductService;
import com.example.library.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class CartController {
  private final CustomerService customerService;

  private final ShoppingCartService cartService;

  private final ProductService productService;

  @Autowired
  public CartController(ShoppingCartService cartService,
                        ProductService productService, CustomerService customerService) {
    this.cartService = cartService;
    this.productService = productService;
    this.customerService = customerService;
  }

  @GetMapping("/cart")
  public String cart(Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }

    Customer customer = customerService.findByUsername(principal.getName());
    ShoppingCart shoppingCart = customer.getCart();

    if (shoppingCart == null) {
      model.addAttribute("check", "No items in your cart");
    }
    model.addAttribute("shoppingCart", shoppingCart);
    return "cart";
  }

  @PostMapping("/add-to-cart")
  public String addItemToCart(@RequestParam("id") Long productId,
      @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
      Principal principal, HttpServletRequest request) {
    if (principal == null) {
      return "redirect:/login";
    }

    Product product = productService.getProductById(productId);
    Customer customer = customerService.findByUsername(principal.getName());
    cartService.addItemToCart(product, quantity, customer);
    return "redirect:" + request.getHeader("Referer");
  }
}