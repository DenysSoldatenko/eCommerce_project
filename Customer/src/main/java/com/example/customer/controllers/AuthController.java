package com.example.customer.controllers;

import com.example.customer.utils.CustomerDtoExceptionManager;
import com.example.library.dtos.CustomerDto;
import com.example.library.services.CustomerService;
import jakarta.validation.Valid;
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

/**
 * Controller class for handling authentication-related operations.
 */
@Controller
public class AuthController {

  private final CustomerService customerService;

  private final BCryptPasswordEncoder passwordEncoder;

  private final CustomerDtoExceptionManager customerDtoExceptionManager;

  /**
   * Constructs an AuthController with the specified dependencies.
   *
   * @param customerService             the CustomerService implementation
   * @param passwordEncoder             the BCryptPasswordEncoder for password encoding
   * @param customerDtoExceptionManager the CustomerDtoExceptionManager for exception handling
   */
  @Autowired
  public AuthController(CustomerService customerService,
                        BCryptPasswordEncoder passwordEncoder,
                        CustomerDtoExceptionManager customerDtoExceptionManager) {
    this.customerService = customerService;
    this.passwordEncoder = passwordEncoder;
    this.customerDtoExceptionManager = customerDtoExceptionManager;
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login() {
    return "login";
  }

  @GetMapping("/register")
  public String register(Model model) {
    model.addAttribute("customerDto", new CustomerDto());
    return "register";
  }

  /**
   * Handles the POST request for processing the registration form.
   *
   * @param customerDto the CustomerDto object containing the registration data
   * @param result      the BindingResult object for validation errors
   * @param model       the model to be populated with attributes
   * @return the view name for the register page or a redirect to the login page
   */
  @PostMapping("/do-register")
  public String processRegister(@Valid @ModelAttribute("customerDto") CustomerDto customerDto,
      BindingResult result, Model model) {

    customerDtoExceptionManager.validate(customerDto, result, model);

    try {
      if (result.hasErrors() || model.containsAttribute("emailError")
          || model.containsAttribute("passwordError")) {
        return "register";
      } else {
        customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        customerService.save(customerDto);
        model.addAttribute("success", "Register successfully");
      }
    } catch (Exception e) {
      model.addAttribute("error", "Server have ran some problems");
      model.addAttribute("customerDto", customerDto);
    }
    return "register";
  }
}