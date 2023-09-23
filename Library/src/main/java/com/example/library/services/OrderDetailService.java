package com.example.library.services;

import com.example.library.models.CartItem;
import com.example.library.models.Order;
import com.example.library.models.OrderDetail;
import java.util.List;
import java.util.Set;

/**
 * Service interface for managing order details.
 */
public interface OrderDetailService {

  List<OrderDetail> createOrderDetailsFromCartItems(Set<CartItem> cartItems, Order order);

  void deleteOrderDetails(List<OrderDetail> orderDetails);
}
