package com.example.library.services.implementations;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.example.library.models.CartItem;
import com.example.library.models.Customer;
import com.example.library.models.Product;
import com.example.library.models.ShoppingCart;
import com.example.library.repositories.ShoppingCartRepository;
import com.example.library.services.CartItemService;
import com.example.library.services.ShoppingCartService;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Implementation of the ShoppingCartService interface.
 */
@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
  private final CartItemService cartItemService;
  private final ShoppingCartRepository shoppingCartRepository;

  @Override
  public void addItemToCart(Product product, int quantity, Customer customer) {
    ShoppingCart cart = getOrCreateShoppingCart(customer);

    CartItem cartItem = cartItemService.findCartItemById(cart.getCartItems(), product.getId());
    if (cartItem == null) {
      cartItemService.createCartItem(product, quantity, cart);
    } else {
      cartItemService.updateCartItem(cartItem, quantity, product);
    }

    int totalItems = cartItemService.calculateTotalItems(cart.getCartItems());
    double totalPrice = cartItemService.calculateTotalPrice(cart.getCartItems());

    updateShoppingCart(cart, totalItems, totalPrice);
  }

  @Override
  public void deleteCartById(Long id) {
    ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Cart not found with id: " + id));

    for (CartItem cartItem : shoppingCart.getCartItems()) {
      cartItemService.deleteCartItemById(cartItem.getId());
    }
    shoppingCart.setCustomer(null);
    shoppingCart.getCartItems().clear();
    shoppingCart.setTotalPrice(0);
    shoppingCart.setTotalItems(0);
    shoppingCartRepository.save(shoppingCart);
  }

  @Override
  public ShoppingCart updateItemInCart(Product product, int quantity, Customer customer) {
    ShoppingCart cart = customer.getCart();
    Set<CartItem> cartItems = cart.getCartItems();
    CartItem item = cartItemService.findCartItemById(cartItems, product.getId());

    item.setQuantity(quantity);
    item.setTotalPrice(quantity * product.getCostPrice());
    cartItemService.saveCartItem(item);

    int totalItems = cartItemService.calculateTotalItems(cartItems);
    double totalPrice = cartItemService.calculateTotalPrice(cartItems);

    cart.setTotalItems(totalItems);
    cart.setTotalPrice(totalPrice);

    return shoppingCartRepository.save(cart);
  }

  @Override
  public ShoppingCart deleteItemFromCart(Product product, Customer customer) {
    ShoppingCart cart = customer.getCart();
    Set<CartItem> cartItems = cart.getCartItems();
    CartItem item = cartItemService.findCartItemById(cartItems, product.getId());

    cartItems.remove(item);
    cartItemService.deleteCartItemById(item.getId());

    int totalItems = cartItemService.calculateTotalItems(cartItems);
    double totalPrice = cartItemService. calculateTotalPrice(cartItems);

    cart.setCartItems(cartItems);
    cart.setTotalItems(totalItems);
    cart.setTotalPrice(totalPrice);

    return shoppingCartRepository.save(cart);
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

  private void updateShoppingCart(ShoppingCart cart, int totalItems, double totalPrice) {
    cart.setTotalItems(totalItems);
    cart.setTotalPrice(totalPrice);
    shoppingCartRepository.save(cart);
  }
}
