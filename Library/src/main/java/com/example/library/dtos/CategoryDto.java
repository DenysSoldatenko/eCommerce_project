package com.example.library.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) class for representing category information.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
  private Long categoryId;
  private String categoryName;
  private Long numberOfProduct;
}
