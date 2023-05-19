package com.example.library.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a shopping cart in the project.
 */
@Data
@AllArgsConstructor
@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "shopping_cart_id")
  private Long id;

  private int totalItems;
  private double totalPrice;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
  private Customer customer;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
  private Set<CartItem> cartItems;

  /**
   * Constructs a new ShoppingCart.
   * Initializes the cart with an empty set of cart items,
   * total items count, and total price.
   */
  public ShoppingCart() {
    this.cartItems = new HashSet<>();
    this.totalItems = 0;
    this.totalPrice = 0.0;
  }
}