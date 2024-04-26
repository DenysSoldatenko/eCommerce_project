package com.example.library.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an order in the project.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Long id;

  @Column(name = "order_date", columnDefinition = "datetime")
  @Temporal(TemporalType.TIMESTAMP)
  private Date orderDate;

  @Column(name = "delivery_date", columnDefinition = "datetime")
  @Temporal(TemporalType.TIMESTAMP)
  private Date deliveryDate;

  private double totalPrice;
  private double tax;
  private int quantity;
  private String paymentMethod;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
  private Customer customer;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
  private List<OrderDetail> orderDetailList;
}