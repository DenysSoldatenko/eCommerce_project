package com.example.customer.controllers;

import com.example.customer.validations.SessionAttributeSetter;
import com.example.library.models.Customer;
import com.example.library.models.Order;
import com.example.library.models.ShoppingCart;
import com.example.library.services.CustomerService;
import com.example.library.services.OrderService;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class OrderController {

  private final CustomerService customerService;
  private final OrderService orderService;
  private final SessionAttributeSetter sessionAttributeSetter;

  /**
   * Retrieves the checkout page.
   *
   * @param model     the model to be populated with attributes
   * @param principal the Principal object representing the currently authenticated user
   * @return the checkout page view with either customer information or failure message
   */
  @GetMapping("/check-out")
  public String checkout(Model model, Principal principal) {
    Customer customer = customerService.findCustomerByUsername(principal.getName());
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


  /**
   * Retrieves the list of orders for the authenticated user.
   *
   * @param model     the model to be populated with attributes
   * @param principal the Principal object representing the currently authenticated user
   * @return the view displaying the list of orders for the user
   *         or a redirect to the login page if not authenticated
   */
  @GetMapping("/orders")
  public String getOrders(Model model, Principal principal) {
    Customer customer = customerService.findCustomerByUsername(principal.getName());
    List<Order> orderList = customer.getOrders();
    model.addAttribute("orders", orderList);
    model.addAttribute("currentTime", new Timestamp(new Date().getTime()));
    model.addAttribute("title", "Order");
    model.addAttribute("page", "Order");
    return "order";
  }

  /**
   * Saves the current shopping cart as an order for the authenticated user.
   *
   * @param principal the Principal object representing the currently authenticated user
   * @return a redirect to the list of orders for the user
   *         or a redirect to the login page if not authenticated
   */
  @GetMapping("/save-order")
  public String saveOrder(Principal principal, HttpSession session) {
    Customer customer = customerService.findCustomerByUsername(principal.getName());
    ShoppingCart cart = customer.getCart();
    orderService.createOrder(cart);
    sessionAttributeSetter.setSessionAttributes(principal, session);
    return "redirect:/orders";
  }

  /**
   * Cancels an order with the specified ID.
   *
   * @param id         the ID of the order to be canceled
   * @param attributes the RedirectAttributes object to add flash attributes for the view
   * @return a redirect to the list of orders for the user with a success message
   */
  @PostMapping("/cancel-order/{id}")
  public String cancelOrder(@PathVariable("id") Long id, RedirectAttributes attributes) {
    orderService.cancelOrder(id);
    attributes.addFlashAttribute("success", "Cancel order successfully!");
    return "redirect:/orders";
  }
}