package com.example.library.dtos;

import com.example.library.models.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) class for representing product information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
  private Long id;
  private String name;
  private String description;
  private double costPrice;
  private int currentQuantity;
  private Category category;
  private String image;
  private boolean activated = true;
  private boolean deleted = false;
}

