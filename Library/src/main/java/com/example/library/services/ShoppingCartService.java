package com.example.library.services;

import com.example.library.models.Customer;
import com.example.library.models.Product;

public interface ShoppingCartService {
  void addItemToCart(Product product, int quantity, Customer customer);
}
