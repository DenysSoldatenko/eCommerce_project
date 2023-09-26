package com.example.library.utils;

import com.example.library.models.Product;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Utility class for uploading images.
 */
@Slf4j
@Component
public class ImageManager {
  private static final String UPLOAD_FOLDER = "Admin/src/main/resources/static/img/image-product";

  /**
   * Uploads an image file.
   *
   * @param imageProduct the image file to upload
   * @return true if the upload is successful, false otherwise
   */
  public boolean uploadImage(MultipartFile imageProduct) {
    try {
      Files.copy(imageProduct.getInputStream(),
          Paths.get(UPLOAD_FOLDER, imageProduct.getOriginalFilename()),
          StandardCopyOption.REPLACE_EXISTING);
      log.info("Image uploaded: {} ", imageProduct.getOriginalFilename());
      return true;
    } catch (Exception e) {
      log.error("Failed to upload image: {} ", imageProduct.getOriginalFilename(), e);
      return false;
    }
  }

  /**
   * Checks if an image file already exists.
   *
   * @param imageProduct the image file to check
   * @return true if the image file exists, false otherwise
   */
  public boolean isImageFileAlreadyUploaded(MultipartFile imageProduct) {
    boolean isExisted = false;
    try {
      File file = new File(UPLOAD_FOLDER + File.separator + imageProduct.getOriginalFilename());
      isExisted = file.exists();
      log.info("Image existed: {} ", imageProduct.getOriginalFilename());
    } catch (Exception e) {
      log.error("Failed to check image existence: {} ", imageProduct.getOriginalFilename(), e);
    }
    return isExisted;
  }

  /**
   * Handles the image for a Product entity during an update operation.
   *
   * @param product      the Product entity to update
   * @param imageProduct the new image file
   */
  public void handleImageOnUpdateOperation(Product product,
                                           MultipartFile imageProduct) throws IOException {
    if (imageProduct.isEmpty()) {
      log.info("No new image provided for Product ID: {}", product.getId());
      product.setImage(product.getImage());
    } else {
      if (!isImageFileAlreadyUploaded(imageProduct)) {
        if (uploadImage(imageProduct)) {
          log.info("Image uploaded for Product ID: {}", product.getId());
          product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
        } else {
          log.error("Failed to upload image for Product ID: {}", product.getId());
        }
      }
    }
  }

  /**
   * Handles the image on create operation for a Product entity.
   *
   * @param product       the Product entity to create
   * @param imageProduct  the image file to upload
   * @throws IOException if an I/O error occurs
   */
  public void handleImageOnCreateOperation(Product product,
                                           MultipartFile imageProduct) throws IOException {
    if (imageProduct != null) {
      if (uploadImage(imageProduct)) {
        log.info("Upload successfully");
      }
      product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
    }
  }
}

