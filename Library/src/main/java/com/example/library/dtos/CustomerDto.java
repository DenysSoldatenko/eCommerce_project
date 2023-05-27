package com.example.library.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) class for representing customer information.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

  @Size(min = 3, max = 15, message = "First name should have 3-15 characters")
  @Pattern(regexp = "^[a-zA-Zа-яА-ЯІіЇїЄєҐґ]+$",
      message = "Invalid first name! (should not contain numbers)")
  private String firstName;

  @Size(min = 3, max = 15, message = "Last name should have 3-15 characters")
  @Pattern(regexp = "^[a-zA-Zа-яА-ЯІіЇїЄєҐґ]+$",
      message = "Invalid last name! (should not contain numbers)")
  private String lastName;

  private String username;

  @Size(min = 5, max = 20, message = "Password should have 5-20 characters")
  private String password;

  private String repeatPassword;
}