package com.example.library.services.implementations;

import com.example.library.models.CartItem;
import com.example.library.models.Customer;
import com.example.library.models.Product;
import com.example.library.models.ShoppingCart;
import com.example.library.repositories.CartItemRepository;
import com.example.library.repositories.ShoppingCartRepository;
import com.example.library.services.ShoppingCartService;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the ShoppingCartService interface.
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
  private final CartItemRepository itemRepository;
  private final ShoppingCartRepository cartRepository;

  @Autowired
  public ShoppingCartServiceImpl(CartItemRepository itemRepository,
                                 ShoppingCartRepository cartRepository) {
    this.itemRepository = itemRepository;
    this.cartRepository = cartRepository;
  }

  @Override
  public void addItemToCart(Product product, int quantity, Customer customer) {
    ShoppingCart cart = getOrCreateShoppingCart(customer);

    CartItem cartItem = findCartItem(cart.getCartItems(), product.getId());
    if (cartItem == null) {
      createCartItem(product, quantity, cart);
    } else {
      updateCartItem(cartItem, quantity, product);
    }

    int totalItems = calculateTotalItems(cart.getCartItems());
    double totalPrice = calculateTotalPrice(cart.getCartItems());

    updateShoppingCart(cart, totalItems, totalPrice);
  }

  @Override
  public ShoppingCart updateItemInCart(Product product, int quantity, Customer customer) {
    ShoppingCart cart = customer.getCart();
    Set<CartItem> cartItems = cart.getCartItems();
    CartItem item = findCartItem(cartItems, product.getId());

    item.setQuantity(quantity);
    item.setTotalPrice(quantity * product.getCostPrice());
    itemRepository.save(item);

    int totalItems = calculateTotalItems(cartItems);
    double totalPrice = calculateTotalPrice(cartItems);

    cart.setTotalItems(totalItems);
    cart.setTotalPrice(totalPrice);

    return cartRepository.save(cart);
  }

  @Override
  public ShoppingCart deleteItemFromCart(Product product, Customer customer) {
    ShoppingCart cart = customer.getCart();
    Set<CartItem> cartItems = cart.getCartItems();
    CartItem item = findCartItem(cartItems, product.getId());

    cartItems.remove(item);
    itemRepository.delete(item);

    int totalItems = calculateTotalItems(cartItems);
    double totalPrice = calculateTotalPrice(cartItems);

    cart.setCartItems(cartItems);
    cart.setTotalItems(totalItems);
    cart.setTotalPrice(totalPrice);

    return cartRepository.save(cart);
  }

  private ShoppingCart getOrCreateShoppingCart(Customer customer) {
    ShoppingCart cart = customer.getCart();
    if (cart == null) {
      cart = new ShoppingCart();
      customer.setCart(cart);
      cart.setCustomer(customer);
    }
    if (cart.getCartItems() == null) {
      cart.setCartItems(new HashSet<>());
    }
    return cart;
  }

  private CartItem findCartItem(Set<CartItem> cartItems, Long productId) {
    if (cartItems == null) {
      return null;
    }
    return cartItems.stream()
    .filter(item -> Objects.equals(item.getProduct().getId(), productId))
    .findFirst().orElse(null);
  }

  private void createCartItem(Product product, int quantity, ShoppingCart cart) {
    CartItem cartItem = new CartItem();
    cartItem.setProduct(product);
    cartItem.setTotalPrice(quantity * product.getCostPrice());
    cartItem.setQuantity(quantity);
    cartItem.setCart(cart);
    itemRepository.save(cartItem);
    cart.getCartItems().add(cartItem);
  }

  private void updateCartItem(CartItem cartItem, int quantity, Product product) {
    cartItem.setQuantity(cartItem.getQuantity() + quantity);
    cartItem.setTotalPrice(cartItem.getTotalPrice() + (quantity * product.getCostPrice()));
    itemRepository.save(cartItem);
  }

  private int calculateTotalItems(Set<CartItem> cartItems) {
    return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
  }

  private double calculateTotalPrice(Set<CartItem> cartItems) {
    return cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
  }

  private void updateShoppingCart(ShoppingCart cart, int totalItems, double totalPrice) {
    cart.setTotalItems(totalItems);
    cart.setTotalPrice(totalPrice);
    cartRepository.save(cart);
  }
}
