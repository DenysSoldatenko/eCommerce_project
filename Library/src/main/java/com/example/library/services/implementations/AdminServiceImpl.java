package com.example.library.services.implementations;

import com.example.library.dtos.AdminDto;
import com.example.library.models.Admin;
import com.example.library.repositories.AdminRepository;
import com.example.library.repositories.RoleRepository;
import com.example.library.services.AdminService;
import java.util.Collections;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Implementation of the AdminService interface.
 */
@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final AdminRepository adminRepository;
  private final RoleRepository roleRepository;
  private final ModelMapper modelMapper;

  @Override
  public Admin findByUsername(String username) {
    return adminRepository.findByUsername(username);
  }

  @Override
  public void createAdmin(AdminDto adminDto) {
    Admin admin = modelMapper.map(adminDto, Admin.class);
    admin.setRoles(Collections.singletonList(roleRepository.findByName("ADMIN")));
    adminRepository.save(admin);
  }
}