package com.example.customer.controllers;

import com.example.customer.validations.AuthExceptionsService;
import com.example.library.dtos.CustomerDto;
import com.example.library.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller class for handling authentication-related operations.
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

  private final CustomerService customerService;
  private final BCryptPasswordEncoder passwordEncoder;
  private final AuthExceptionsService authExceptionsService;

  @GetMapping("/login")
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

    authExceptionsService.validate(customerDto, result, model);

    if (result.hasErrors() || model.containsAttribute("emailError")
        || model.containsAttribute("passwordError")) {
      return "register";
    } else {
      customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
      customerService.createCustomerDto(customerDto);
      model.addAttribute("success", "Register successfully");
    }
    return "register";
  }
}