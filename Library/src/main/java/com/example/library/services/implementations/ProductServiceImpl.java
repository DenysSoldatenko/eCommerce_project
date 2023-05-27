package com.example.library.services.implementations;

import com.example.library.dtos.ProductDto;
import com.example.library.models.Product;
import com.example.library.repositories.ProductRepository;
import com.example.library.services.ProductService;
import com.example.library.utils.ImageUpload;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;

  private final ImageUpload imageUpload;

  private final ModelMapper modelMapper;

  /**
   * Constructs a new ProductServiceImpl with the specified dependencies.
   *
   * @param productRepository the ProductRepository to be used for data access
   * @param imageUpload       the ImageUpload utility for handling image uploads
   * @param modelMapper       the ModelMapper for object mapping
   */

  @Autowired
  public ProductServiceImpl(ProductRepository productRepository,
                            ImageUpload imageUpload, ModelMapper modelMapper) {
    this.productRepository = productRepository;
    this.imageUpload = imageUpload;
    this.modelMapper = modelMapper;
  }

  @Override
  public List<ProductDto> findAll() {
    List<Product> products = productRepository.findAll();

    return products.stream()
    .map(product -> modelMapper.map(product, ProductDto.class))
    .collect(Collectors.toList());
  }

  @Override
  public Product save(MultipartFile imageProduct, ProductDto productDto) {
    Product product = new Product();

    try {
      handleImageOnCreate(product, imageProduct);
    } catch (IOException e) {
      e.printStackTrace();
    }

    mapProductDtoToProduct(productDto, product);
    return productRepository.save(product);
  }

  @Override
  public Product update(MultipartFile imageProduct, ProductDto productDto) {
    Product product = productRepository.findById(productDto.getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Product not found with id: " + productDto.getId()));

    try {
      handleImageOnUpdate(product, imageProduct);
    } catch (IOException e) {
      e.printStackTrace();
    }

    mapProductDtoToProduct(productDto, product);
    return productRepository.save(product);
  }

  @Override
  public void deleteById(Long id) {
    Optional<Product> product = productRepository.findById(id);
    if (product.isPresent()) {
      product.get().setDeleted(true);
      product.get().setActivated(false);
      productRepository.save(product.get());
    }
  }

  @Override
  public void enableById(Long id) {
    Optional<Product> product = productRepository.findById(id);
    if (product.isPresent()) {
      product.get().setActivated(true);
      product.get().setDeleted(false);
      productRepository.save(product.get());
    }
  }

  @Override
  public Page<Product> pageProducts(int pageNo) {
    Pageable pageable = PageRequest.of(pageNo, 5);
    return productRepository.findAllProducts(pageable);
  }

  @Override
  public Page<Product> searchProducts(int pageNo, String keyword) {
    Pageable pageable = PageRequest.of(pageNo, 5);
    return productRepository.findAllByNameOrDescription(keyword, pageable);
  }

  @Override
  public ProductDto getById(Long id) {
    Optional<Product> product = productRepository.findById(id);

    if (product.isPresent()) {
      return modelMapper.map(product.get(), ProductDto.class);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + id);
    }
  }

  /**
   * Handles the image on update operation for a Product entity.
   *
   * @param product       the Product entity to update
   * @param imageProduct  the new image file
   * @throws IOException if an I/O error occurs
   */
  public void handleImageOnUpdate(Product product, MultipartFile imageProduct) throws IOException {
    if (imageProduct.isEmpty()) {
      product.setImage(product.getImage());
    } else {
      if (!imageUpload.checkExisted(imageProduct)) {
        imageUpload.uploadImage(imageProduct);
      }
      product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
    }
  }

  /**
   * Handles the image on create operation for a Product entity.
   *
   * @param product       the Product entity to create
   * @param imageProduct  the image file to upload
   * @throws IOException if an I/O error occurs
   */
  public void handleImageOnCreate(Product product, MultipartFile imageProduct) throws IOException {
    if (imageProduct != null) {
      if (imageUpload.uploadImage(imageProduct)) {
        System.out.println("Upload successfully");
      }
      product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
    }
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
  public List<Product> getAllProducts() {
    return productRepository.getAllProducts();
  }

  @Override
  public Page<Product> listViewProducts(int pageNo) {
    Pageable pageable = PageRequest.of(pageNo, 5);
    return productRepository.listViewProducts(pageable);
  }

  @Override
  public Product getProductById(Long id) {
    return productRepository.findById(id)
    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
    "Product not found with id: " + id));
  }

  @Override
  public List<Product> getRelatedProducts(Long categoryId) {
    return productRepository.getRelatedProducts(categoryId);
  }

  @Override
  public List<Product> getProductsInCategory(Long categoryId) {
    return productRepository.getProductsInCategory(categoryId);
  }

  @Override
  public List<Product> filterHighPrice() {
    return productRepository.filterHighPrice();
  }

  @Override
  public List<Product> filterLowPrice() {
    return productRepository.filterLowPrice();
  }
}