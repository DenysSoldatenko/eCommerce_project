package com.example.library.services;

import com.example.library.dtos.ProductDto;
import com.example.library.models.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  Page<Product> pageProducts(int pageNo);

  Page<Product> searchProducts(int pageNo, String keyword);

  ProductDto getById(Long id);


  /*Customer*/
  List<Product> getAllProducts();

  Page<Product> listViewProducts(int pageNo);

  Product getProductById(Long id);

  List<Product> getRelatedProducts(Long categoryId);

  List<Product> getProductsInCategory(Long categoryId);
}

