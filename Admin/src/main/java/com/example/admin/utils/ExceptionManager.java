package com.example.admin.utils;

import com.example.library.dtos.AdminDto;
import com.example.library.models.Admin;
import com.example.library.services.implementations.AdminServiceImpl;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/**
 * Utility class for managing exceptions and validation in the admin module.
 */
@Component
public class ExceptionManager {

  private final AdminServiceImpl adminService;

  @Autowired
  public ExceptionManager(AdminServiceImpl adminService) {
    this.adminService = adminService;
  }

  /**
   * Validates the AdminDto object and adds error attributes
   * to the model based on the validation result.
   *
   * @param adminDto  the AdminDto object to be validated
   * @param result    the BindingResult object for validation result
   * @param model     the model object to add error attributes
   */
  public void validate(AdminDto adminDto, BindingResult result, Model model) {
    handleException(adminDto, result, model);
    handleEmail(adminDto, model);
    handlePassword(adminDto, model);
  }

  /**
   * Handles any general validation errors and adds the adminDto to the model if there are errors.
   *
   * @param adminDto  the AdminDto object to check for validation errors
   * @param result    the BindingResult object for validation result
   * @param model     the model object to add the adminDto attribute
   */
  public void handleException(AdminDto adminDto, BindingResult result, Model model) {
    if (result.hasErrors()) {
      model.addAttribute("adminDto", adminDto);
      System.out.println(result);
    }
  }

  /**
   * Handles the email validation and adds error attributes
   * to the model if the email is already registered.
   *
   * @param adminDto  the AdminDto object to check for email validation
   * @param model     the model object to add the error attributes
   */
  public void handleEmail(AdminDto adminDto, Model model) {
    String username = adminDto.getUsername();
    Optional<Admin> admin = Optional.ofNullable(adminService.findByUsername(username));

    if (admin.isPresent()) {
      model.addAttribute("adminDto", adminDto);
      model.addAttribute("emailError", "Your email has been registered!");
    }
  }

  /**
   * Handles the password validation and adds an error attribute
   * to the model if the passwords do not match.
   *
   * @param adminDto  the AdminDto object to check for password validation
   * @param model     the model object to add the error attribute
   */
  public void handlePassword(AdminDto adminDto, Model model) {
    if (!adminDto.getPassword().equals(adminDto.getRepeatPassword())) {
      model.addAttribute("adminDto", adminDto);
      model.addAttribute("passwordError", "Your password may be wrong! Check again!");
    }
  }
}
