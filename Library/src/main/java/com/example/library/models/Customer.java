package com.example.library.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a customer in the project.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
@Table(name = "customers", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "customer_id")
  private Long id;

  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private String phoneNumber;
  private String address;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "name", referencedColumnName = "id")
  private City city;

  private String country;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "customer_role",
      joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customer_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
  private Collection<Role> roles;

  @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
  private ShoppingCart cart;

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
  private List<Order> orders;

  public Customer() {
    this.cart = new ShoppingCart();
    this.orders = new ArrayList<>();
  }
}
