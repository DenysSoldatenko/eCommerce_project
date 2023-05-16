package com.example.library.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a cart item in the project.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_detail_id")
  private Long id;

  private int quantity;
  private double totalPrice;
  private double unitPrice;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "shopping_cart_id", referencedColumnName = "shopping_cart_id")
  private ShoppingCart cart;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "product_id", referencedColumnName = "product_id")
  private Product product;
}