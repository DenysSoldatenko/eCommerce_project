package com.example.library.services.implementations;

import com.example.library.dtos.ProductDto;
import com.example.library.models.Product;
import com.example.library.repositories.ProductRepository;
import com.example.library.services.ProductService;
import com.example.library.utils.ImageManager;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 * Implementation of the ProductService interface.
 */
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;

  private final ImageManager imageManager;

  private final ModelMapper modelMapper;

  @Override
  public List<ProductDto> findAll() {
    List<Product> products = productRepository.findAll();

    return products.stream()
    .map(product -> modelMapper.map(product, ProductDto.class))
    .collect(Collectors.toList());
  }

  @Override
  public void createProduct(MultipartFile imageProduct, ProductDto productDto) throws IOException {
    Product product = new Product();
    imageManager.handleImageOnCreateOperation(product, imageProduct);
    mapProductDtoToProduct(productDto, product);
    productRepository.save(product);
  }

  @Override
  public void updateProduct(MultipartFile imageProduct, ProductDto productDto) throws IOException {
    Product product = productRepository.findById(productDto.getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Product not found with id: " + productDto.getId()));

    imageManager.handleImageOnUpdateOperation(product, imageProduct);
    mapProductDtoToProduct(productDto, product);
    productRepository.save(product);
  }

  @Override
  public void deleteProductById(Long id) {
    productRepository.findById(id).ifPresent(product -> {
      product.setDeleted(true);
      product.setActivated(false);
      productRepository.save(product);
    });
  }

  @Override
  public void enableProductById(Long id) {
    productRepository.findById(id).ifPresent(product -> {
      product.setActivated(true);
      product.setDeleted(false);
      productRepository.save(product);
    });
  }

  @Override
  public Page<Product> findAllProductsPaginated(int pageNo) {
    Pageable pageable = PageRequest.of(pageNo, 5);
    return productRepository.findAllProductsPaginated(pageable);
  }

  @Override
  public List<Product> findAllProductsBySearch(String keyword) {
    return productRepository.searchProductsByNameOrDescription(keyword);
  }

  @Override
  public Page<Product> findAllProductsPaginatedBySearch(int pageNo, String keyword) {
    Pageable pageable = PageRequest.of(pageNo, 5);
    return productRepository.findAllByNameOrDescriptionPaginated(keyword, pageable);
  }

  @Override
  public ProductDto findProductDetailsById(Long id) {
    return productRepository.findById(id)
    .map(product -> modelMapper.map(product, ProductDto.class))
    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
      "Product not found with id: " + id));
  }

  private void mapProductDtoToProduct(ProductDto productDto, Product product) {
    modelMapper.getConfiguration()
      .setPropertyCondition(Conditions.isNotNull())
      .setMatchingStrategy(MatchingStrategies.STANDARD)
      .setFieldMatchingEnabled(true)
        .setSkipNullEnabled(true);

    product.setActivated(true);
    product.setDeleted(false);

    modelMapper.map(productDto, product);
  }


  /*Customer*/
  @Override
  public List<Product> findAllProductsForCustomer() {
    return productRepository.findActivatedAndNotDeletedProducts();
  }

  @Override
  public Page<Product> findAllProductsPaginatedForCustomer(int pageNo) {
    Pageable pageable = PageRequest.of(pageNo, 5);
    return productRepository.findAllViewableProductsPaginated(pageable);
  }

  @Override
  public Product findProductById(Long id) {
    return productRepository.findById(id)
    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
    "Product not found with id: " + id));
  }

  @Override
  public List<Product> findRelatedProductsById(Long categoryId) {
    return productRepository.findProductsByCategoryId(categoryId);
  }

  @Override
  public List<Product> findProductsInCategoryById(Long categoryId) {
    return productRepository.getProductsInCategory(categoryId);
  }

  @Override
  public List<Product> filterProductsByHighPrice() {
    return productRepository.filterProductsByHighPrice();
  }

  @Override
  public List<Product> filterProductsByLowPrice() {
    return productRepository.filterProductsByLowPrice();
  }
}