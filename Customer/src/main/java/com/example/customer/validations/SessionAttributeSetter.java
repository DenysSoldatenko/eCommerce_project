package com.example.customer.validations;

import com.example.library.models.Customer;
import com.example.library.models.ShoppingCart;
import com.example.library.services.CustomerService;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Utility class for setting session attributes.
 */
@Component
@RequiredArgsConstructor
public class SessionAttributeSetter {

  private final CustomerService customerService;

  /**
   * Sets session attributes based on shopping cart items.
   *
   * @param principal the Principal object representing the currently authenticated user
   * @param session   the HttpSession to set the session attributes
   */
  public void setSessionAttributes(Principal principal, HttpSession session) {
    if (principal != null) {
      Customer customer = customerService.findCustomerByUsername(principal.getName());
      session.setAttribute("username", customer.getFirstName());
      ShoppingCart shoppingCart = customer.getCart();
      if (shoppingCart != null) {
        session.setAttribute("totalItems", shoppingCart.getTotalItems());
      }
    }
  }
}
