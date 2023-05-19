package com.example.customer.configurations;

import com.example.library.models.Customer;
import com.example.library.repositories.CustomerRepository;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService implementation for loading Customer details.
 */
@Service
public class CustomerServiceConfig implements UserDetailsService {

  private final CustomerRepository customerRepository;

  @Autowired
  public CustomerServiceConfig(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Customer customer = customerRepository.findByUsername(username);
    if (customer == null) {
      throw new UsernameNotFoundException("Could not find username");
    }

    return new User(
      customer.getUsername(),
      customer.getPassword(),
      customer.getRoles()
      .stream()
      .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
    );
  }
}