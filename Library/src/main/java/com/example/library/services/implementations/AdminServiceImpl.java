package com.example.library.services.implementations;

import com.example.library.dtos.AdminDto;
import com.example.library.models.Admin;
import com.example.library.repositories.AdminRepository;
import com.example.library.repositories.RoleRepository;
import com.example.library.services.AdminService;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the AdminService interface.
 */
@Service
public class AdminServiceImpl implements AdminService {
  private final AdminRepository adminRepository;

  private final RoleRepository roleRepository;

  @Autowired
  public AdminServiceImpl(AdminRepository adminRepository, RoleRepository roleRepository) {
    this.adminRepository = adminRepository;
    this.roleRepository = roleRepository;
  }

  @Override
  public Admin findByUsername(String username) {
    return adminRepository.findByUsername(username);
  }

  @Override
  public Admin save(AdminDto adminDto) {
    Admin admin = new Admin();
    admin.setFirstName(adminDto.getFirstName());
    admin.setLastName(adminDto.getLastName());
    admin.setUsername(adminDto.getUsername());
    admin.setPassword(adminDto.getPassword());
    admin.setRoles(Collections.singletonList(roleRepository.findByName("ADMIN")));
    return adminRepository.save(admin);
  }
}