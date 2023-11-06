package com.example.customer.validations;

import com.example.library.dtos.CustomerDto;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/**
 * Service interface for handling authentication-related exceptions
 * and validating CustomerDto objects.
 */
public interface AuthExceptionsService {

  void validate(CustomerDto customerDto, BindingResult result, Model model);

  void handleException(CustomerDto customerDto, BindingResult result, Model model);

  void handleEmail(CustomerDto customerDto, Model model);

  void handlePassword(CustomerDto customerDto, Model model);
}
