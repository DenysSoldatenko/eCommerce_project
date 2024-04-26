package com.example.library.services;

import com.example.library.models.ShoppingCart;

/**
 * Service interface for managing orders.
 */
public interface OrderService {

  void createOrder(ShoppingCart shoppingCart);

  void cancelOrder(Long id);
}
