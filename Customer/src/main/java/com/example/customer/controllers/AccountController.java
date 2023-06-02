package com.example.customer.controllers;

import com.example.customer.utils.CustomerExceptionManager;
import com.example.library.models.Customer;
import com.example.library.services.CustomerService;
import java.security.Principal;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller class for handling account-related actions.
 */
@Controller
public class AccountController {
  private final CustomerService customerService;
  private final CustomerExceptionManager customerExceptionManager;

  @Autowired
  public AccountController(CustomerService customerService,
                           CustomerExceptionManager customerExceptionManager) {
    this.customerService = customerService;
    this.customerExceptionManager = customerExceptionManager;
  }

  /**
   * Retrieves the account home page.
   *
   * @param model     the model to be populated with attributes
   * @param principal the Principal object representing the currently authenticated user
   * @return the account home page view
   */
  @GetMapping("/account")
  public String accountHome(Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }

    String username = principal.getName();
    Customer customer = customerService.findByUsername(username);
    model.addAttribute("customer", customer);
    return "account";
  }

  /**
   * Updates the customer account information.
   *
   * @param customer   the Customer object to update
   * @param attributes the RedirectAttributes object for adding flash attributes
   * @return the account home page view with success or fail message
   */
  @RequestMapping(value = "/update-account", method = {RequestMethod.GET, RequestMethod.PUT})
  public String updateCustomer(@ModelAttribute("customer") Customer customer,
                               RedirectAttributes attributes) {
    customerExceptionManager.validate(customer);

    if (Objects.equals(customerExceptionManager.getMessage(), "")) {
      customerService.saveInfo(customer);
      attributes.addFlashAttribute("success", "Added successfully");
    } else {
      attributes.addFlashAttribute("fail", customerExceptionManager.getMessage());
      return "redirect:/account";
    }
    return "redirect:/account";
  }
}
