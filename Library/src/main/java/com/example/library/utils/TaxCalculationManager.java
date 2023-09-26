package com.example.library.utils;

import com.example.library.models.ShoppingCart;
import org.springframework.stereotype.Service;

/**
 * Service for calculating taxes based on the contents of a shopping cart.
 */
@Service
public class TaxCalculationManager {

  private static final double STANDARD_TAX_RATE = 0.05;
  private static final double HIGH_TAX_RATE = 0.1;
  private static final int HIGH_PRODUCT_COUNT_THRESHOLD = 10;

  public double calculateTax(ShoppingCart shoppingCart) {
    int productCount = shoppingCart.getTotalItems();
    return (productCount > HIGH_PRODUCT_COUNT_THRESHOLD) ? HIGH_TAX_RATE : STANDARD_TAX_RATE;
  }
}
