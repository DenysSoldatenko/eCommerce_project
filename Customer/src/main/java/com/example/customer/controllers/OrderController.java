package com.example.customer.controllers;

import com.example.library.models.Customer;
import com.example.library.models.Order;
import com.example.library.models.ShoppingCart;
import com.example.library.services.CustomerService;
import java.security.Principal;
import java.util.List;

import com.example.library.services.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller class for handling order-related actions.
 */
@Controller
public class OrderController {

  private final CustomerService customerService;
  private final OrderService orderService;
  
  @Autowired
  public OrderController(CustomerService customerService, OrderService orderService) {
    this.customerService = customerService;
    this.orderService = orderService;
  }

  /**
   * Retrieves the checkout page.
   *
   * @param model     the model to be populated with attributes
   * @param principal the Principal object representing the currently authenticated user
   * @return the checkout page view with either customer information or failure message
   */
  @GetMapping("/check-out")
  public String checkout(Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }

    Customer customer = customerService.findByUsername(principal.getName());
    if (customer.getPhoneNumber() == null || customer.getAddress() == null) {
      model.addAttribute("customer", customer);
      model.addAttribute("fail", "You must fill the information after checkout!");
      return "account";
    } else {
      model.addAttribute("customer", customer);
      model.addAttribute("cart", customer.getCart());
    }
    return "checkout";
  }

  @GetMapping("/orders")
  public String getOrders(Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    } else {
      Customer customer = customerService.findByUsername(principal.getName());
      List<Order> orderList = customer.getOrders();
      model.addAttribute("orders", orderList);
      model.addAttribute("title", "Order");
      model.addAttribute("page", "Order");
      return "order";
    }
  }

  @GetMapping("/save-order")
  public String saveOrder(Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    } else {
      Customer customer = customerService.findByUsername(principal.getName());
      ShoppingCart cart = customer.getCart();
      orderService.save(cart);
      return "redirect:/orders";
    }
  }

  @PostMapping("/cancel-order/{id}")
  public String cancelOrder(@PathVariable("id") Long id, RedirectAttributes attributes) {
    orderService.cancelOrder(id);
    attributes.addFlashAttribute("success", "Cancel order successfully!");
    return "redirect:/orders";
  }
}