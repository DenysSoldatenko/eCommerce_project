package com.example.library.services.implementations;

import com.example.library.dtos.ProductDto;
import com.example.library.models.Product;
import com.example.library.repositories.ProductRepository;
import com.example.library.services.ProductService;
import com.example.library.utils.ImageUpload;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  public ProductServiceImpl(ProductRepository productRepository, ImageUpload imageUpload) {
    this.productRepository = productRepository;
    this.imageUpload = imageUpload;
  }

  @Override
  public List<ProductDto> findAll() {
    List<ProductDto> productDtoList = new ArrayList<>();
    List<Product> products = productRepository.findAll();
    for (Product product : products) {
      ProductDto productDto = new ProductDto();
      setProductDtoData(product, productDto);
      productDtoList.add(productDto);
    }
    return productDtoList;
  }

  @Override
  public Product save(MultipartFile imageProduct, ProductDto productDto) {
    Product product = new Product();
    try {
      handleImageOnCreate(product, imageProduct);
    } catch (IOException e) {
      e.printStackTrace();
    }
    product.setName(productDto.getName());
    product.setDescription(productDto.getDescription());
    product.setCategory(productDto.getCategory());
    product.setCostPrice(productDto.getCostPrice());
    product.setCurrentQuantity(productDto.getCurrentQuantity());
    product.setActivated(true);
    product.setDeleted(false);
    return productRepository.save(product);
  }

  @Override
  public Product update(MultipartFile imageProduct, ProductDto productDto) {
    Product product = productRepository.getById(productDto.getId());
    try {
      handleImageOnUpdate(product, imageProduct);
    } catch (IOException e) {
      e.printStackTrace();
    }
    product.setName(productDto.getName());
    product.setDescription(productDto.getDescription());
    product.setSalePrice(productDto.getSalePrice());
    product.setCostPrice(productDto.getCostPrice());
    product.setCurrentQuantity(productDto.getCurrentQuantity());
    product.setCategory(productDto.getCategory());
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
  public Optional<Product> findById(Long id) {
    return productRepository.findById(id);
  }

  @Override
  public ProductDto getById(Long id) {
    Optional<Product> product = productRepository.findById(id);
    if (product.isPresent()) {
      ProductDto productDto = new ProductDto();
      setProductDtoData(product.get(), productDto);
      return productDto;
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + id);
    }
  }

  public void setProductDtoData(Product product, ProductDto productDto) {
    productDto.setId(product.getId());
    productDto.setName(product.getName());
    productDto.setDescription(product.getDescription());
    productDto.setCurrentQuantity(product.getCurrentQuantity());
    productDto.setCategory(product.getCategory());
    productDto.setSalePrice(product.getSalePrice());
    productDto.setCostPrice(product.getCostPrice());
    productDto.setImage(product.getImage());
    productDto.setDeleted(product.isDeleted());
    productDto.setActivated(product.isActivated());
  }

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

  public void handleImageOnCreate(Product product, MultipartFile imageProduct) throws IOException {
    if (imageProduct != null) {
      if (imageUpload.uploadImage(imageProduct)) {
        System.out.println("Upload successfully");
      }
      product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
    }
  }
}