package com.example.library.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) class for representing cart item information.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
  private Long id;

  private ShoppingCartDto cart;

  private ProductDto product;

  private int quantity;

  private double unitPrice;
}
