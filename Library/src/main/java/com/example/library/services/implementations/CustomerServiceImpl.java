package com.example.library.services.implementations;

import com.example.library.dtos.CustomerDto;
import com.example.library.models.Customer;
import com.example.library.repositories.CustomerRepository;
import com.example.library.repositories.RoleRepository;
import com.example.library.services.CustomerService;
import java.util.Collections;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Implementation of the CustomerService interface.
 */
@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final RoleRepository repository;
  private final CustomerRepository customerRepository;
  private final ModelMapper modelMapper;

  @Override
  public void createCustomerDto(CustomerDto customerDto) {
    Customer customer = modelMapper.map(customerDto, Customer.class);
    customer.setRoles(Collections.singletonList(repository.findByName("CUSTOMER")));
    Customer savedCustomer = customerRepository.save(customer);
    modelMapper.map(savedCustomer, CustomerDto.class);
  }

  @Override
  public void createCustomer(Customer customer) {
    customerRepository.save(customer);
  }

  @Override
  public Customer findCustomerByUsername(String username) {
    return customerRepository.findByUsername(username);
  }

  @Override
  public void updateCustomerInfo(Customer c) {
    Customer customer = customerRepository.findByUsername(c.getUsername());
    customer.setAddress(c.getAddress());
    customer.setPhoneNumber(c.getPhoneNumber());
    customerRepository.save(customer);
  }

  @Override
  public CustomerDto convertToDto(Customer customer) {
    return modelMapper.map(customer, CustomerDto.class);
  }
}