package com.example.customer.controllers;

import com.example.library.models.Customer;
import com.example.library.models.Product;
import com.example.library.models.ShoppingCart;
import com.example.library.services.CustomerService;
import com.example.library.services.ProductService;
import com.example.library.services.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller class for handling shopping-cart-related operations.
 */
@Controller
@RequiredArgsConstructor
public class CartController {

  private final CustomerService customerService;
  private final ShoppingCartService cartService;
  private final ProductService productService;

  /**
   * Handles the request for displaying the shopping cart.
   *
   * @param model     the model to be populated with attributes
   * @param principal the principal object representing the authenticated user
   * @param session   the HttpSession object for storing session data
   * @return the view name for displaying the shopping cart
   *         or a redirect to the login page if not authenticated
   */
  @GetMapping("/cart")
  public String cart(Model model, Principal principal, HttpSession session) {
    Customer customer = customerService.findCustomerByUsername(principal.getName());
    ShoppingCart shoppingCart = customer.getCart();

    if (shoppingCart == null || shoppingCart.getCartItems().isEmpty()) {
      model.addAttribute("check", "No items in your cart");
    }

    session.setAttribute("totalItems", Optional.ofNullable(shoppingCart)
        .map(ShoppingCart::getTotalItems).orElse(0));
    model.addAttribute("subTotal", Optional.ofNullable(shoppingCart)
        .map(ShoppingCart::getTotalPrice).orElse(0.00));

    model.addAttribute("shoppingCart", shoppingCart);
    return "cart";
  }

  /**
   * Handles the request for adding an item to the shopping cart.
   *
   * @param productId the ID of the product to add to the cart
   * @param quantity  the quantity of the product to add
   * @param principal the principal object representing the authenticated user
   * @param request   the HttpServletRequest object for retrieving the request header
   * @return a redirect to the previous page
   *         or a redirect to the login page if not authenticated
   */
  @PostMapping("/add-to-cart")
  public String addItemToCart(
      @RequestParam("id") Long productId,
      @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
      Principal principal, HttpServletRequest request
  ) {
    Product product = productService.findProductById(productId);
    Customer customer = customerService.findCustomerByUsername(principal.getName());
    cartService.addItemToCart(product, quantity, customer);
    return "redirect:" + request.getHeader("Referer");
  }

  /**
   * Handles the request
   * for updating the quantity of an item in the shopping cart.
   *
   * @param quantity  the new quantity of the item
   * @param productId the ID of the product to update
   * @param model     the model to be populated with attributes
   * @param principal the principal object representing the authenticated user
   * @return a redirect to the cart page
   *         or a redirect to the login page if not authenticated
   */
  @PostMapping("/update-cart")
  public String updateCart(
      @RequestParam("quantity") int quantity,
      @RequestParam("id") Long productId,
      Model model, Principal principal
  ) {

    if (quantity > productService.findProductById(productId).getCurrentQuantity()) {
      model.addAttribute("check", "Requested quantity exceeds available stock!");
      return "cart";
    }

    String username = principal.getName();
    Customer customer = customerService.findCustomerByUsername(username);
    Product product = productService.findProductById(productId);
    ShoppingCart cart = cartService.updateItemInCart(product, quantity, customer);

    model.addAttribute("shoppingCart", cart);
    return "redirect:/cart";
  }

  /**
   * Handles the request for deleting an item from the shopping cart.
   *
   * @param productId the ID of the product to delete
   * @param model     the model to be populated with attributes
   * @param principal the principal object representing the authenticated user
   * @return a redirect to the cart page
   *         or a redirect to the login page if not authenticated
   */
  @PostMapping("/delete-cart")
  public String deleteItemFromCart(
      @RequestParam("id") Long productId,
      Model model, Principal principal
  ) {
    String username = principal.getName();
    Customer customer = customerService.findCustomerByUsername(username);
    Product product = productService.findProductById(productId);
    ShoppingCart cart = cartService.deleteItemFromCart(product, customer);

    model.addAttribute("shoppingCart", cart);
    return "redirect:/cart";
  }
}