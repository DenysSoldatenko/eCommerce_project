package com.example.library.services;

import com.example.library.dtos.CustomerDto;
import com.example.library.models.Customer;

/**
 * Service interface for managing customers.
 */
public interface CustomerService {

  CustomerDto save(CustomerDto customerDto);

  Customer save(Customer customer);

  Customer findByUsername(String username);

  void saveInfo(Customer customer);

  CustomerDto getCustomerDto(Customer customer);
}
