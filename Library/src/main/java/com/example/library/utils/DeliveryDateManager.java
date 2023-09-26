package com.example.library.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.springframework.stereotype.Service;

/**
 * Service for generating delivery dates based on the number of products in an order.
 */
@Service
public class DeliveryDateManager {

  private static final int STANDARD_DELIVERY_DAYS = 1;
  private static final int LONG_DELIVERY_DAYS = 7;
  private static final int HIGH_PRODUCT_COUNT_THRESHOLD = 5;

  /**
   * Generate a delivery date for an order based on the number of products in the order.
   *
   * @param productCount The number of products in the order.
   * @return A Date object representing the delivery date.
   */
  public Date generateDeliveryDate(int productCount) {
    int deliveryDays = (productCount > HIGH_PRODUCT_COUNT_THRESHOLD) ? LONG_DELIVERY_DAYS
        : STANDARD_DELIVERY_DAYS;
    return calculateDeliveryDate(new Date(), deliveryDays);
  }

  private Date calculateDeliveryDate(Date orderDate, int deliveryDays) {
    Instant instant = orderDate.toInstant().plus(deliveryDays, ChronoUnit.DAYS);
    return Date.from(instant);
  }
}
