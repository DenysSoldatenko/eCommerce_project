package com.example.library.services.implementations;

import com.example.library.dtos.CustomerDto;
import com.example.library.models.Customer;
import com.example.library.repositories.CustomerRepository;
import com.example.library.repositories.RoleRepository;
import com.example.library.services.CustomerService;
import java.util.Collections;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the CustomerService interface.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

  private final RoleRepository repository;

  private final CustomerRepository customerRepository;

  private final ModelMapper modelMapper;

  /**
   * Constructs a CustomerServiceImpl with the specified dependencies.
   *
   * @param repository          the RoleRepository for managing roles
   * @param customerRepository the CustomerRepository for database access
   * @param modelMapper        the ModelMapper for object mapping
   */
  @Autowired
  public CustomerServiceImpl(RoleRepository repository,
      CustomerRepository customerRepository, ModelMapper modelMapper) {
    this.repository = repository;
    this.customerRepository = customerRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public CustomerDto save(CustomerDto customerDto) {
    Customer customer = modelMapper.map(customerDto, Customer.class);
    customer.setRoles(Collections.singletonList(repository.findByName("CUSTOMER")));
    Customer savedCustomer = customerRepository.save(customer);
    return modelMapper.map(savedCustomer, CustomerDto.class);
  }

  @Override
  public Customer save(Customer customer) {
    return customerRepository.save(customer);
  }

  @Override
  public Customer findByUsername(String username) {
    return customerRepository.findByUsername(username);
  }

  @Override
  public void saveInfo(Customer c) {
    Customer customer = customerRepository.findByUsername(c.getUsername());
    customer.setAddress(c.getAddress());
    customer.setPhoneNumber(c.getPhoneNumber());
    customerRepository.save(customer);
  }

  @Override
  public CustomerDto getCustomerDto(Customer customer) {
    return modelMapper.map(customer, CustomerDto.class);
  }
}