package com.example.admin.configurations;

import com.example.library.models.Admin;
import com.example.library.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService implementation for loading Admin details.
 */
@Service
@RequiredArgsConstructor
public class AdminServiceConfig implements UserDetailsService {

  private final AdminRepository adminRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Admin admin = adminRepository.findByUsername(username);

    if (admin == null) {
      throw new UsernameNotFoundException("Could not find username");
    }

    return new User(
      admin.getUsername(),
      admin.getPassword(),
      admin.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList()
    );
  }
}
