package com.example.library.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Utility class for uploading images.
 */
@Component
public class ImageUpload {

  /**
   * Uploads an image file.
   *
   * @param imageProduct the image file to upload
   * @return true if the upload is successful, false otherwise
   */
  public boolean uploadImage(MultipartFile imageProduct) {
    boolean isUpload = false;
    try {
      String uploadFolder = "Admin/src/main/resources/static/img/image-product";
      Files.copy(imageProduct.getInputStream(),
          Paths.get(uploadFolder + File.separator, imageProduct.getOriginalFilename()),
          StandardCopyOption.REPLACE_EXISTING);
      isUpload = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return isUpload;
  }
}

