package com.example.library.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) class for representing admin information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {

  @Size(min = 3, max = 10, message = "Invalid first name!(3-10 characters)")
  @Pattern(regexp = "^[a-zA-Zа-яА-ЯІіЇїЄєҐґ]+$",
      message = "Invalid first name! (should not contain numbers)")
  private String firstName;

  @Size(min = 3, max = 10, message = "Invalid last name!(3-10 characters)")
  @Pattern(regexp = "^[a-zA-Zа-яА-ЯІіЇїЄєҐґ]+$",
      message = "Invalid last name! (should not contain numbers)")
  private String lastName;

  private String username;

  @Size(min = 5, max = 15, message = "Invalid password !(5-15 characters)")
  private String password;

  private String repeatPassword;
}