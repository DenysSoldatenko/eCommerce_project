package com.example.library.services;

import com.example.library.dtos.AdminDto;
import com.example.library.models.Admin;

/**
 * Service interface for managing Admin entities.
 */
public interface AdminService {

  void createAdmin(AdminDto adminDto);

  Admin findByUsername(String username);
}
