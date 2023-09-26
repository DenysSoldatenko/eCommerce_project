package com.example.library.services.implementations;

import com.example.library.models.CartItem;
import com.example.library.models.Order;
import com.example.library.models.OrderDetail;
import com.example.library.repositories.OrderDetailRepository;
import com.example.library.services.OrderDetailService;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the OrderDetailService interface.
 */
@Service
@AllArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
  private final OrderDetailRepository orderDetailRepository;

  @Override
  public List<OrderDetail> createOrderDetailsFromCartItems(Set<CartItem> cartItems, Order order) {
    List<OrderDetail> orderDetails = cartItems.stream()
        .map(item -> createOrderDetail(item, order))
        .toList();

    orderDetailRepository.saveAll(orderDetails);
    return orderDetails;
  }

  private OrderDetail createOrderDetail(CartItem item, Order order) {
    OrderDetail orderDetail = new OrderDetail();
    orderDetail.setOrder(order);
    orderDetail.setQuantity(item.getQuantity());
    orderDetail.setTotalPrice(item.getTotalPrice());
    orderDetail.setProduct(item.getProduct());
    return orderDetail;
  }

  @Override
  public void deleteOrderDetails(List<OrderDetail> orderDetails) {
    orderDetailRepository.deleteAll(orderDetails);
  }
}
