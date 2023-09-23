package com.example.library.services;

import com.example.library.dtos.ProductDto;
import com.example.library.models.Product;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for managing products.
 */
public interface ProductService {
  List<ProductDto> findAll();

  void createProduct(MultipartFile imageProduct, ProductDto productDto) throws IOException;

  void updateProduct(MultipartFile imageProduct, ProductDto productDto) throws IOException;

  void deleteProductById(Long id);

  void enableProductById(Long id);

  Page<Product> findAllProductsPaginated(int pageNo);

  Page<Product> findAllProductsPaginatedBySearch(int pageNo, String keyword);

  List<Product> findAllProductsBySearch(String keyword);

  ProductDto findProductDetailsById(Long id);


  /*Customer*/
  List<Product> findAllProductsForCustomer();

  Page<Product> findAllProductsPaginatedForCustomer(int pageNo);

  Product findProductById(Long id);

  List<Product> findRelatedProductsById(Long categoryId);

  List<Product> findProductsInCategoryById(Long categoryId);

  List<Product> filterProductsByHighPrice();

  List<Product> filterProductsByLowPrice();
}

