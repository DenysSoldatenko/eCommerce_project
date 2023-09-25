package com.example.admin.validations.impl;

import com.example.admin.validations.ProductDtoExceptionService;
import com.example.library.dtos.ProductDto;
import com.example.library.services.ProductService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Utility class for handling exceptions related to the Product.
 */
@Component
@RequiredArgsConstructor
public class ProductDtoExceptionServiceImpl implements ProductDtoExceptionService {

  private final ProductService productService;

  @Getter
  private String errorMessage;

  /**
   * Validates the product and image file for any empty or invalid fields.
   *
   * @param product       the product DTO to validate
   * @param imageProduct  the image file to validate
   */
  @Override
  public void validate(ProductDto product, MultipartFile imageProduct) {
    errorMessage = "";
    handleEmptyName(product);
    handleEmptyCategory(product);
    handleEmptyQuantity(product);
    handleEmptyPrice(product);
    handleEmptyImage(product, imageProduct);
  }

  @Override
  public void handleEmptyName(ProductDto product) {
    if (product.getName() == null || product.getName().equals("")) {
      errorMessage = "Failed to perform because empty name";
    }
  }

  @Override
  public void handleEmptyCategory(ProductDto product) {
    if (product.getCategory() == null) {
      errorMessage = "Failed to perform because empty category";
    }
  }

  @Override
  public void handleEmptyQuantity(ProductDto product) {
    try {
      int quantity = Integer.parseInt(String.valueOf(product.getCurrentQuantity()));
      if (quantity <= 0) {
        errorMessage = "Failed to perform because of an invalid quantity";
      }
    } catch (NumberFormatException e) {
      errorMessage = "Failed to perform because the quantity is not a valid number";
    }
  }

  @Override
  public void handleEmptyPrice(ProductDto product) {
    try {
      double price = Double.parseDouble(String.valueOf(product.getCostPrice()));
      if (price <= 0) {
        errorMessage = "Failed to perform because of an invalid price";
      }
    } catch (NumberFormatException e) {
      errorMessage = "Failed to perform because the price is not a valid number";
    }
  }

  @Override
  public void handleEmptyImage(ProductDto productDto, MultipartFile imageProduct) {
    if (productDto.getId() == null) {
      if (imageProduct.isEmpty()) {
        errorMessage = "Failed to perform create action because empty image";
      }
    } else {
      ProductDto product = productService.findProductDetailsById(productDto.getId());
      if (imageProduct.isEmpty() && product.getImage().isEmpty()) {
        errorMessage = "Failed to perform update action because empty image";
      }
    }
  }
}
