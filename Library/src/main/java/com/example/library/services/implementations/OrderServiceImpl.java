package com.example.library.services.implementations;

import com.example.library.models.Order;
import com.example.library.models.OrderDetail;
import com.example.library.models.ShoppingCart;
import com.example.library.repositories.OrderRepository;
import com.example.library.services.OrderDetailService;
import com.example.library.services.OrderService;
import com.example.library.services.ShoppingCartService;
import com.example.library.utils.DeliveryDateManager;
import com.example.library.utils.TaxCalculationManager;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the OrderService interface.
 */
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final OrderDetailService orderDetailService;
  private final ShoppingCartService cartService;
  private final TaxCalculationManager taxService;
  private final DeliveryDateManager deliveryDateService;

  @Override
  public void createOrder(ShoppingCart shoppingCart) {
    Order order = buildOrderFromCart(shoppingCart);
    List<OrderDetail> orderDetails
        = orderDetailService.createOrderDetailsFromCartItems(shoppingCart.getCartItems(), order);
    order.setOrderDetailList(orderDetails);
    cartService.deleteCartById(shoppingCart.getId());
    orderRepository.save(order);
  }

  @Override
  public void cancelOrder(Long orderId) {
    Optional<Order> orderOptional = orderRepository.findById(orderId);
    orderOptional.ifPresent(order -> {
      orderDetailService.deleteOrderDetails(order.getOrderDetailList());
      orderRepository.delete(order);
    });
  }

  private Order buildOrderFromCart(ShoppingCart shoppingCart) {
    return Order.builder()
    .orderDate(new Date())
    .customer(shoppingCart.getCustomer())
    .tax(taxService.calculateTax(shoppingCart))
    .totalPrice(shoppingCart.getTotalPrice())
    .deliveryDate(deliveryDateService.generateDeliveryDate(shoppingCart.getTotalItems()))
    .paymentMethod("Cash")
    .quantity(shoppingCart.getTotalItems())
    .build();
  }
}
