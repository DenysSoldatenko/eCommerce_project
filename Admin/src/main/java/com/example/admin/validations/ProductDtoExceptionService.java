package com.example.admin.validations;

import com.example.library.dtos.ProductDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for handling exceptions and validations related to ProductDto objects.
 */
public interface ProductDtoExceptionService {

  void validate(ProductDto product, MultipartFile imageProduct);

  void handleEmptyName(ProductDto product);

  void handleEmptyCategory(ProductDto product);

  void handleEmptyQuantity(ProductDto product);

  void handleEmptyPrice(ProductDto product);

  void handleEmptyImage(ProductDto productDto, MultipartFile imageProduct);

  String getErrorMessage();
}
