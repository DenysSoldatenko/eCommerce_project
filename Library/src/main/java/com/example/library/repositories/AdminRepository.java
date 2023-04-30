package com.example.library.repositories;

import com.example.library.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing Admin entities.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
  Admin findByUsername(String username);
}
