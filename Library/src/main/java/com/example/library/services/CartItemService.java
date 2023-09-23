package com.example.library.services;

import com.example.library.models.CartItem;
import com.example.library.models.Product;
import com.example.library.models.ShoppingCart;
import java.util.Set;

/**
 * Service interface for managing cart items.
 */
public interface CartItemService {

  CartItem findCartItemById(Set<CartItem> cartItems, Long productId);

  void updateCartItem(CartItem cartItem, int quantity, Product product);

  int calculateTotalItems(Set<CartItem> cartItems);

  double calculateTotalPrice(Set<CartItem> cartItems);

  void deleteCartItemById(Long id);

  void saveCartItem(CartItem item);

  void createCartItem(Product product, int quantity, ShoppingCart cart);
}
