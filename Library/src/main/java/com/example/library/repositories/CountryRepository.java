package com.example.library.repositories;

import com.example.library.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing Country entities.
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

}
