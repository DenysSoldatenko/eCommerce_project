package com.example.library.services.implementations;

import com.example.library.dtos.AdminDto;
import com.example.library.models.Admin;
import com.example.library.repositories.AdminRepository;
import com.example.library.repositories.RoleRepository;
import com.example.library.services.AdminService;
import java.util.Collections;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the AdminService interface.
 */
@Service
public class AdminServiceImpl implements AdminService {
  private final AdminRepository adminRepository;

  private final RoleRepository roleRepository;

  private final ModelMapper modelMapper;

  /**
   * Constructs an AdminServiceImpl with the specified dependencies.
   *
   * @param adminRepository the AdminRepository for database access
   * @param roleRepository  the RoleRepository for managing roles
   * @param modelMapper     the ModelMapper for object mapping
   */
  @Autowired
  public AdminServiceImpl(AdminRepository adminRepository,
                          RoleRepository roleRepository, ModelMapper modelMapper) {
    this.adminRepository = adminRepository;
    this.roleRepository = roleRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public Admin findByUsername(String username) {
    return adminRepository.findByUsername(username);
  }

  @Override
  public Admin save(AdminDto adminDto) {
    Admin admin = modelMapper.map(adminDto, Admin.class);
    admin.setRoles(Collections.singletonList(roleRepository.findByName("ADMIN")));
    return adminRepository.save(admin);
  }
}