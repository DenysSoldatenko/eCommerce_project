package com.example.customer.validations;

import com.example.library.models.Customer;

/**
 * Service for handling exceptions and validations related to Customer objects.
 */
public interface CustomerExceptionService {

  void validate(Customer customer);

  void handlePhoneNumber(Customer customer);

  void handleAddress(Customer customer);

  String getErrorMessage();
}
