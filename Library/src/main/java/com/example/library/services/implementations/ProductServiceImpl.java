package com.example.library.services.implementations;

import com.example.library.dtos.ProductDto;
import com.example.library.models.Product;
import com.example.library.repositories.ProductRepository;
import com.example.library.services.ProductService;
import com.example.library.utils.ImageUpload;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
      productDto.setId(product.getId());
      productDto.setName(product.getName());
      productDto.setDescription(product.getDescription());
      productDto.setCostPrice(product.getCostPrice());
      productDto.setSalePrice(product.getSalePrice());
      productDto.setCurrentQuantity(product.getCurrentQuantity());
      productDto.setCategory(product.getCategory());
      productDto.setImage(product.getImage());
      productDto.setActivated(product.isActivated());
      productDto.setDeleted(product.isDeleted());
      productDtoList.add(productDto);
    }
    return productDtoList;
  }

  @Override
  public Product save(MultipartFile imageProduct, ProductDto productDto) {
    try {
      Product product = new Product();
      if (imageProduct == null) {
        product.setImage(null);
      } else {
        if (imageUpload.uploadImage(imageProduct)) {
          System.out.println("Upload successfully");
        }
        product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
      }
      product.setName(productDto.getName());
      product.setDescription(productDto.getDescription());
      product.setCategory(productDto.getCategory());
      product.setCostPrice(productDto.getCostPrice());
      product.setCurrentQuantity(productDto.getCurrentQuantity());
      product.setActivated(true);
      product.setDeleted(false);
      return productRepository.save(product);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }

  @Override
  public Product update(ProductDto productDto) {
    return null;
  }

  @Override
  public void deleteById(Long id) { }

  @Override
  public void enableById(Long id) { }
}

