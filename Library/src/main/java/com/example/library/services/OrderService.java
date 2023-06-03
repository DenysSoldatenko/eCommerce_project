package com.example.library.services;

import com.example.library.models.Order;
import com.example.library.models.ShoppingCart;

/**
 * Service interface for managing orders.
 */
public interface OrderService {
  Order save(ShoppingCart shoppingCart);

  void cancelOrder(Long id);
}
