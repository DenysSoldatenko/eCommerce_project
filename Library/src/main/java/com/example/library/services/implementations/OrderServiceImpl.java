package com.example.library.services.implementations;

import com.example.library.models.CartItem;
import com.example.library.models.Order;
import com.example.library.models.OrderDetail;
import com.example.library.models.ShoppingCart;
import com.example.library.repositories.OrderDetailRepository;
import com.example.library.repositories.OrderRepository;
import com.example.library.services.OrderService;
import com.example.library.services.ShoppingCartService;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the OrderService interface.
 */
@Service
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final OrderDetailRepository detailRepository;
  private final ShoppingCartService cartService;

  /**
   * Constructs a new OrderServiceImpl with the provided dependencies.
   *
   * @param orderRepository   the OrderRepository for accessing order-related data
   * @param detailRepository  the OrderDetailRepository for accessing order detail data
   * @param cartService       the ShoppingCartService for handling shopping cart operations
   */
  @Autowired
  public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository detailRepository,
      ShoppingCartService cartService) {
    this.orderRepository = orderRepository;
    this.detailRepository = detailRepository;
    this.cartService = cartService;
  }

  @Override
  public Order save(ShoppingCart shoppingCart) {
    Order order = createOrderFromCart(shoppingCart);
    List<OrderDetail> list = createOrderDetailsFromCartItems(shoppingCart.getCartItems(), order);
    order.setOrderDetailList(list);
    cartService.deleteCartById(shoppingCart.getId());
    return orderRepository.save(order);
  }

  @Override
  public void cancelOrder(Long id) {
    Order order = orderRepository.findById(id).orElse(null);
    if (order != null) {
      deleteOrderDetails(order.getOrderDetailList());
      orderRepository.delete(order);
    }
  }

  private Order createOrderFromCart(ShoppingCart shoppingCart) {
    Order order = new Order();
    order.setOrderDate(new Date());
    order.setCustomer(shoppingCart.getCustomer());
    order.setTax(2);
    order.setTotalPrice(shoppingCart.getTotalPrice());
    order.setAccept(false);
    order.setDeliveryDate(new Timestamp(Instant.now().plus(Duration.ofDays(2)).toEpochMilli()));
    order.setPaymentMethod("Cash");
    order.setQuantity(shoppingCart.getTotalItems());
    return order;
  }

  private List<OrderDetail> createOrderDetailsFromCartItems(Set<CartItem> cartItems, Order order) {
    List<OrderDetail> orderDetailList = new ArrayList<>();
    for (CartItem item : cartItems) {
      OrderDetail orderDetail = new OrderDetail();
      orderDetail.setOrder(order);
      orderDetail.setQuantity(item.getQuantity());
      orderDetail.setTotalPrice(item.getTotalPrice());
      orderDetail.setProduct(item.getProduct());
      detailRepository.save(orderDetail);
      orderDetailList.add(orderDetail);
    }
    return orderDetailList;
  }

  private void deleteOrderDetails(List<OrderDetail> orderDetails) {
    detailRepository.deleteAll(orderDetails);
  }
}