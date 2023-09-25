package com.example.admin.validations;

import com.example.library.dtos.AdminDto;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/**
 * Service for handling exceptions and validations related to AdminDto objects.
 */
public interface AdminDtoExceptionService {

  void validate(AdminDto adminDto, BindingResult result, Model model);

  void handleException(AdminDto adminDto, BindingResult result, Model model);

  void handleEmail(AdminDto adminDto, Model model);

  void handlePassword(AdminDto adminDto, Model model);
}
