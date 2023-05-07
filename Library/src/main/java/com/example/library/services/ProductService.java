package com.example.library.services;

import com.example.library.dtos.ProductDto;
import com.example.library.models.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for managing products.
 */
public interface ProductService {
  List<ProductDto> findAll();

  Product save(MultipartFile imageProduct, ProductDto productDto);

  Product update(MultipartFile imageProduct, ProductDto productDto);

  void deleteById(Long id);

  void enableById(Long id);

  Optional<Product> findById(Long id);

  ProductDto getById(Long id);
}

