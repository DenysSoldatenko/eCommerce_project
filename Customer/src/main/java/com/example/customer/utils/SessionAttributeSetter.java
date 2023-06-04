package com.example.customer.utils;

import com.example.library.models.Customer;
import com.example.library.models.ShoppingCart;
import com.example.library.services.CustomerService;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Utility class for setting session attributes.
 */
@Component
public class SessionAttributeSetter {
  private final CustomerService customerService;

  @Autowired
  public SessionAttributeSetter(CustomerService customerService) {
    this.customerService = customerService;
  }

  /**
   * Sets session attributes based on shopping cart items.
   *
   * @param principal the Principal object representing the currently authenticated user
   * @param session   the HttpSession to set the session attributes
   */
  public void setSessionAttributes(Principal principal, HttpSession session) {
    if (principal != null) {
      Customer customer = customerService.findByUsername(principal.getName());
      ShoppingCart shoppingCart = customer.getCart();
      if (shoppingCart != null) {
        session.setAttribute("totalItems", shoppingCart.getTotalItems());
      }
    }
  }
}
