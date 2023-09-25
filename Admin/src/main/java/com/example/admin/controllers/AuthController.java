package com.example.admin.controllers;

import com.example.admin.validations.AdminDtoExceptionService;
import com.example.library.dtos.AdminDto;
import com.example.library.services.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class for handling login, registration, and other related actions.
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

  private final AdminDtoExceptionService adminDtoExceptionService;
  private final BCryptPasswordEncoder passwordEncoder;
  private final AdminService adminService;

  @GetMapping("/login")
  public String login(Model model) {
    model.addAttribute("title", "Login");
    return "login";
  }

  /**
   * Handles the request for the home page.
   *
   * @param model the model to be populated with attributes
   * @return the view name for the home page or a redirect to the login page if not authenticated
   */
  @RequestMapping("/index")
  public String home(Model model) {
    model.addAttribute("title", "Home Page");
    return "index";
  }

  /**
   * Handles GET requests to "/register" URL and renders the register view.
   *
   * @param model the model object to be passed to the view
   * @return the register view name
   */
  @GetMapping("/register")
  public String register(Model model) {
    model.addAttribute("title", "Register");
    model.addAttribute("adminDto", new AdminDto());
    return "register";
  }

  @GetMapping("/forgot-password")
  public String forgotPassword(Model model) {
    model.addAttribute("title", "Forgot Password");
    return "forgot-password";
  }

  /**
   * Handles POST requests to "/register-new" URL and processes the registration of a new admin.
   *
   * @param adminDto  the AdminDto object containing the admin's information
   * @param result    the BindingResult object for validation result
   * @param model     the model object to be passed to the view
   * @return the register view name
   */
  @PostMapping("/register-new")
  public String addNewAdmin(@Valid @ModelAttribute("adminDto") AdminDto adminDto,
                            BindingResult result, Model model) {

    adminDtoExceptionService.validate(adminDto, result, model);

    if (result.hasErrors() || model.containsAttribute("emailError")
        || model.containsAttribute("passwordError")) {
      return "register";
    } else {
      adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
      adminService.createAdmin(adminDto);
      model.addAttribute("success", "Register successfully!");
      model.addAttribute("adminDto", adminDto);
    }
    return "register";
  }
}
