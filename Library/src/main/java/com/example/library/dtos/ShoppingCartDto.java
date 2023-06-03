package com.example.library.dtos;

import com.example.library.models.Customer;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) class for representing shopping cart information.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto {
  private Long id;

  private Customer customer;

  private double totalPrice;

  private int totalItems;

  private Set<CartItemDto> cartItems;
}
