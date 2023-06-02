package com.example.customer.controllers;

import com.example.library.models.Customer;
import com.example.library.services.CustomerService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for handling order-related actions.
 */
@Controller
public class OrderController {

  private final CustomerService customerService;
  
  @Autowired
  public OrderController(CustomerService customerService) {
    this.customerService = customerService;
  }

  /**
   * Retrieves the checkout page.
   *
   * @param model     the model to be populated with attributes
   * @param principal the Principal object representing the currently authenticated user
   * @return the checkout page view with either customer information or failure message
   */
  @GetMapping("/check-out")
  public String checkout(Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }

    Customer customer = customerService.findByUsername(principal.getName());
    if (customer.getPhoneNumber() == null || customer.getAddress() == null) {
      model.addAttribute("customer", customer);
      model.addAttribute("fail", "You must fill the information after checkout!");
      return "account";
    } else {
      model.addAttribute("customer", customer);
      model.addAttribute("cart", customer.getCart());
    }
    return "checkout";
  }

  @GetMapping("/order")
  public String order() {
    return "order";
  }

}