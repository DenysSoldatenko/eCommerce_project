package com.example.admin.controllers;

import com.example.library.dtos.ProductDto;
import com.example.library.models.Category;
import com.example.library.services.CategoryService;
import com.example.library.services.ProductService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for managing products.
 */
@Controller
public class ProductController {

  private final ProductService productService;

  private final CategoryService categoryService;

  @Autowired
  public ProductController(ProductService productService, CategoryService categoryService) {
    this.productService = productService;
    this.categoryService = categoryService;
  }

  /**
   * Displays the list of products.
   *
   * @param model     the model to be populated
   * @param principal the currently authenticated user
   * @return the view name
   */
  @GetMapping("/products")
  public String products(Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    List<ProductDto> productDtoList = productService.findAll();
    List<Category> categories = categoryService.findAllByActivated();
    model.addAttribute("title", "Manage Product");
    model.addAttribute("categories", categories);
    model.addAttribute("product", new ProductDto());
    model.addAttribute("products", productDtoList);
    model.addAttribute("size", productDtoList.size());
    return "products";
  }

  /**
   * Adds a new product.
   *
   * @param productDto       the product DTO to be added
   * @param imageProduct     the image file of the product
   * @param attributes       the redirect attributes
   * @return the redirect URL
   */
  @PostMapping("/add-product")
  public String addProduct(@ModelAttribute("product") ProductDto productDto,
                           @RequestParam("imageProduct") MultipartFile imageProduct,
                           RedirectAttributes attributes) {
    try {
      productService.save(imageProduct, productDto);
      attributes.addFlashAttribute("success", "Added successfully!");
    } catch (Exception e) {
      e.printStackTrace();
      attributes.addFlashAttribute("error", "Failed to add!");
    }
    return "redirect:/products";
  }
}
