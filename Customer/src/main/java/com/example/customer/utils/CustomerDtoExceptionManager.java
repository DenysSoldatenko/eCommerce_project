package com.example.customer.utils;

import com.example.library.dtos.CustomerDto;
import com.example.library.models.Customer;
import com.example.library.services.implementations.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/**
 * Utility class for handling exceptions related to the Customer DTO.
 */
@Component
public class CustomerDtoExceptionManager {

  private final CustomerServiceImpl customerService;

  @Autowired
  public CustomerDtoExceptionManager(CustomerServiceImpl customerService) {
    this.customerService = customerService;
  }

  /**
   * Validates the CustomerDto and handles any binding result errors.
   *
   * @param customerDto the CustomerDto to validate
   * @param result      the BindingResult containing validation errors
   * @param model       the model to be populated with attributes
   */
  public void validate(CustomerDto customerDto, BindingResult result, Model model) {
    handleException(customerDto, result, model);
    handleEmail(customerDto, model);
    handlePassword(customerDto, model);
  }

  /**
   * Handles the exception when there are binding result errors.
   *
   * @param customerDto the CustomerDto object to be handled
   * @param result      the BindingResult object containing validation errors
   * @param model       the model to be populated with attributes
   */
  public void handleException(CustomerDto customerDto, BindingResult result, Model model) {
    if (result.hasErrors()) {
      model.addAttribute("customerDto", customerDto);
    }
  }

  /**
   * Handles the validation of the email in the CustomerDto.
   * Checks if the email is already registered.
   *
   * @param customerDto the CustomerDto containing the email to validate
   * @param model       the model to be populated with attributes
   */
  public void handleEmail(CustomerDto customerDto, Model model) {
    Customer customer = customerService.findByUsername(customerDto.getUsername());
    if (customer != null) {
      model.addAttribute("emailError", "Your email has been registered!");
      model.addAttribute("customerDto", customerDto);
    }
  }

  /**
   * Handles the validation of the password in the CustomerDto.
   * Checks if the password matches the repeated password.
   *
   * @param customerDto the CustomerDto containing the password to validate
   * @param model       the model to be populated with attributes
   */
  public void handlePassword(CustomerDto customerDto, Model model) {
    if (!customerDto.getPassword().equals(customerDto.getRepeatPassword())) {
      model.addAttribute("passwordError", "Your password may be wrong! Check again!");
      model.addAttribute("customerDto", customerDto);
    }
  }
}
