package com.example.admin.controllers;

import com.example.admin.utils.ExceptionManager;
import com.example.library.dtos.AdminDto;
import com.example.library.services.implementations.AdminServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LoginController {
  private final ExceptionManager exceptionManager;
  private final AdminServiceImpl adminService;
  private final BCryptPasswordEncoder passwordEncoder;

  /**
   * Constructs a new instance of the LoginController.
   *
   * @param exceptionManager  the ExceptionManager for handling exceptions
   * @param adminService      the AdminServiceImpl for admin-related operations
   * @param passwordEncoder   the BCryptPasswordEncoder for encoding passwords
   */
  @Autowired
  public LoginController(ExceptionManager exceptionManager, AdminServiceImpl adminService,
                         BCryptPasswordEncoder passwordEncoder) {
    this.exceptionManager = exceptionManager;
    this.adminService = adminService;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping("/login")
  public String login(Model model) {
    model.addAttribute("title", "Login");
    return "login";
  }

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

    exceptionManager.validate(adminDto, result, model);

    try {
      if (result.hasErrors() || model.containsAttribute("emailError")
          || model.containsAttribute("passwordError")) {
        return "register";
      } else {
        adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        adminService.save(adminDto);
        model.addAttribute("success", "Register successfully!");
        model.addAttribute("adminDto", adminDto);
      }
    } catch (Exception e) {
      e.printStackTrace();
      model.addAttribute("errors", "The server has been wrong!");
    }
    return "register";
  }
}
