package com.example.library.services.implementations;

import com.example.library.models.Order;
import com.example.library.models.OrderDetail;
import com.example.library.models.Product;
import com.example.library.models.ShoppingCart;
import com.example.library.repositories.OrderRepository;
import com.example.library.repositories.ProductRepository;
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
  private final ProductRepository productService;

  @Override
  public void createOrder(ShoppingCart shoppingCart) {
    Order order = buildOrderFromCart(shoppingCart);
    List<OrderDetail> orderDetails
        = orderDetailService.createOrderDetailsFromCartItems(shoppingCart.getCartItems(), order);
    order.setOrderDetailList(orderDetails);
    updateProductQuantities(order.getOrderDetailList(), false);
    cartService.deleteCartById(shoppingCart.getId());
    orderRepository.save(order);
  }

  @Override
  public void cancelOrder(Long orderId) {
    Optional<Order> orderOptional = orderRepository.findById(orderId);
    orderOptional.ifPresent(order -> {
      orderDetailService.deleteOrderDetails(order.getOrderDetailList());
      orderRepository.delete(order);
      updateProductQuantities(order.getOrderDetailList(), true);
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

  private void updateProductQuantities(List<OrderDetail> orderDetails, boolean returnQuantities) {
    for (OrderDetail orderDetail : orderDetails) {
      Product product = orderDetail.getProduct();
      int changeQuantity = orderDetail.getQuantity();

      if (returnQuantities) {
        product.setCurrentQuantity(product.getCurrentQuantity() + changeQuantity);
        product.setActivated(true);
      } else {
        product.setCurrentQuantity(product.getCurrentQuantity() - changeQuantity);
        if (product.getCurrentQuantity() == 0) {
          product.setActivated(false);
        }
      }

      productService.save(product);
    }
  }
}
