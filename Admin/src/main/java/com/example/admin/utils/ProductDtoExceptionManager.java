package com.example.admin.utils;

import com.example.library.dtos.ProductDto;
import com.example.library.services.ProductService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Utility class for handling exceptions related to the Product.
 */
@Component
@Getter
public class ProductDtoExceptionManager {
  private final ProductService productService;
  private String message;

  @Autowired
  public ProductDtoExceptionManager(ProductService productService) {
    this.productService = productService;
  }

  /**
   * Validates the product and image file for any empty or invalid fields.
   *
   * @param product       the product DTO to validate
   * @param imageProduct  the image file to validate
   */
  public void validate(ProductDto product, MultipartFile imageProduct) {
    message = "";
    handleEmptyName(product);
    handleEmptyCategory(product);
    handleEmptyQuantity(product);
    handleEmptyPrice(product);
    handleEmptyImage(product, imageProduct);
  }

  private void handleEmptyName(ProductDto product) {
    if (product.getName() == null || product.getName().equals("")) {
      message = "Failed to perform because empty name";
    }
  }

  private void handleEmptyCategory(ProductDto product) {
    if (product.getCategory() == null) {
      message = "Failed to perform because empty category";
    }
  }

  private void handleEmptyQuantity(ProductDto product) {
    try {
      int quantity = Integer.parseInt(String.valueOf(product.getCurrentQuantity()));
      if (quantity <= 0) {
        message = "Failed to perform because of an invalid quantity";
      }
    } catch (NumberFormatException e) {
      message = "Failed to perform because the quantity is not a valid number";
    }
  }

  private void handleEmptyPrice(ProductDto product) {
    try {
      double price = Double.parseDouble(String.valueOf(product.getCostPrice()));
      if (price <= 0) {
        message = "Failed to perform because of an invalid price";
      }
    } catch (NumberFormatException e) {
      message = "Failed to perform because the price is not a valid number";
    }
  }

  private void handleEmptyImage(ProductDto productDto, MultipartFile imageProduct) {
    if (productDto.getId() == null) {
      if (imageProduct.isEmpty()) {
        message = "Failed to perform create action because empty image";
      }
    } else {
      ProductDto product = productService.getById(productDto.getId());
      if (imageProduct.isEmpty() && product.getImage().isEmpty()) {
        message = "Failed to perform update action because empty image";
      }
    }
  }
}
