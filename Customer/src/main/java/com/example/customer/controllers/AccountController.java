package com.example.customer.controllers;

import com.example.customer.utils.AuthExceptionManager;
import com.example.customer.utils.CustomerExceptionManager;
import com.example.library.dtos.CustomerDto;
import com.example.library.models.Customer;
import com.example.library.services.CustomerService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

  private final BCryptPasswordEncoder passwordEncoder;

  private final AuthExceptionManager authExceptionManager;

  /**
   * Constructs a new AccountController with the specified dependencies.
   *
   * @param customerService          the CustomerService to handle customer-related operations
   * @param customerExceptionManager the CustomerExceptionManager for handling customer exceptions
   * @param passwordEncoder          the BCryptPasswordEncoder for password encoding
   * @param authExceptionManager     the AuthExceptionManager for handling authentication exceptions
   */
  @Autowired
  public AccountController(CustomerService customerService,
                           CustomerExceptionManager customerExceptionManager,
                           BCryptPasswordEncoder passwordEncoder,
                           AuthExceptionManager authExceptionManager) {
    this.customerService = customerService;
    this.customerExceptionManager = customerExceptionManager;
    this.passwordEncoder = passwordEncoder;
    this.authExceptionManager = authExceptionManager;
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
    Customer customer = customerService.findCustomerByUsername(username);
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
      customerService.updateCustomerInfo(customer);
      attributes.addFlashAttribute("success", "Added successfully");
    } else {
      attributes.addFlashAttribute("fail", customerExceptionManager.getMessage());
      return "redirect:/account";
    }
    return "redirect:/account";
  }

  /**
   * Displays the change password page.
   *
   * @param model     the model to be populated with attributes
   * @param principal the Principal object representing the currently authenticated user
   * @return the change password page view
   */
  @GetMapping("/change-password")
  public String passwordHome(Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }

    Customer customer = customerService.findCustomerByUsername(principal.getName());
    CustomerDto customerDto = customerService.convertToDto(customer);
    model.addAttribute("customerDto", customerDto);
    return "change-password";
  }

  /**
   * Handles the request for updating the customer password.
   *
   * @param customerDto the CustomerDto object containing the updated password
   * @param result      the BindingResult object for validation results
   * @param model       the Model object to be populated with attributes
   * @return the change password page view
   */
  @PostMapping("/update-password")
  public String updatePassword(@Valid @ModelAttribute("customerDto") CustomerDto customerDto,
                               BindingResult result, Model model) {
    authExceptionManager.validate(customerDto, result, model);
    validatePasswordAndUpdate(customerDto, result, model);
    return "change-password";
  }

  private void validatePasswordAndUpdate(CustomerDto customerDto,
                                         BindingResult result, Model model) {
    try {
      if (!result.hasErrors() && !model.containsAttribute("passwordError")) {
        Customer existingCustomer = customerService.findCustomerByUsername(customerDto.getUsername());
        existingCustomer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        customerService.createCustomer(existingCustomer);
        model.addAttribute("success", "Password updated successfully");
      }
    } catch (Exception e) {
      model.addAttribute("error", "Server encountered some problems");
      model.addAttribute("customerDto", customerDto);
    }
  }
}
