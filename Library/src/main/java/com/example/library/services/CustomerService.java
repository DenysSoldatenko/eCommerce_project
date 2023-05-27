package com.example.library.services;

import com.example.library.dtos.CustomerDto;
import com.example.library.models.Customer;

/**
 * Service interface for managing customers.
 */
public interface CustomerService {

  CustomerDto save(CustomerDto customerDto);

  Customer findByUsername(String username);

  Customer saveInfo(Customer customer);
}
