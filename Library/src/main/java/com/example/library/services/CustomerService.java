package com.example.library.services;

import com.example.library.dtos.CustomerDto;
import com.example.library.models.Customer;

/**
 * Service interface for managing customers.
 */
public interface CustomerService {

  void createCustomerDto(CustomerDto customerDto);

  void createCustomer(Customer customer);

  void updateCustomerInfo(Customer customer);

  Customer findCustomerByUsername(String username);

  CustomerDto convertToDto(Customer customer);
}
