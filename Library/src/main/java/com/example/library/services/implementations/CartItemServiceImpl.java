package com.example.library.services.implementations;

import com.example.library.models.CartItem;
import com.example.library.models.Product;
import com.example.library.models.ShoppingCart;
import com.example.library.repositories.CartItemRepository;
import com.example.library.services.CartItemService;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the CartItemService interface.
 */
@Service
@AllArgsConstructor
public class CartItemServiceImpl implements CartItemService {

  private final CartItemRepository itemRepository;

  @Override
  public CartItem findCartItemById(Set<CartItem> cartItems, Long productId) {
    if (cartItems == null) {
      return null;
    }
    return cartItems.stream()
    .filter(item -> Objects.equals(item.getProduct().getId(), productId))
    .findFirst().orElse(null);
  }

  @Override
  public void createCartItem(Product product, int quantity, ShoppingCart cart) {
    CartItem cartItem = new CartItem();
    cartItem.setProduct(product);
    cartItem.setTotalPrice(quantity * product.getCostPrice());
    cartItem.setQuantity(quantity);
    cartItem.setCart(cart);
    itemRepository.save(cartItem);
    cart.getCartItems().add(cartItem);
  }

  @Override
  public void updateCartItem(CartItem cartItem, int quantity, Product product) {
    cartItem.setQuantity(cartItem.getQuantity() + quantity);
    cartItem.setTotalPrice(cartItem.getTotalPrice() + (quantity * product.getCostPrice()));
    itemRepository.save(cartItem);
  }

  @Override
  public int calculateTotalItems(Set<CartItem> cartItems) {
    return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
  }

  @Override
  public double calculateTotalPrice(Set<CartItem> cartItems) {
    return cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
  }

  @Override
  public void saveCartItem(CartItem item) {
    itemRepository.save(item);
  }

  @Override
  public void deleteCartItemById(Long id) {
    itemRepository.deleteById(id);
  }
}
