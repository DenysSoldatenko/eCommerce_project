package com.example.library.services;

import com.example.library.models.Customer;
import com.example.library.models.Product;
import com.example.library.models.ShoppingCart;

/**
 * Service interface for managing shopping carts.
 */
public interface ShoppingCartService {
  void addItemToCart(Product product, int quantity, Customer customer);

  ShoppingCart updateItemInCart(Product product, int quantity, Customer customer);

  ShoppingCart deleteItemFromCart(Product product, Customer customer);
}
