package com.example.admin.controllers;

import com.example.admin.utils.ProductDtoExceptionManager;
import com.example.library.dtos.ProductDto;
import com.example.library.models.Category;
import com.example.library.services.CategoryService;
import com.example.library.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

/**
 * Controller for managing products.
 */
@Controller
public class ProductController {

  private final ProductService productService;

  private final CategoryService categoryService;

  private final ProductDtoExceptionManager productDtoExceptionManager;

  @Autowired
  public ProductController(ProductService productService,
                           CategoryService categoryService,
                           ProductDtoExceptionManager productDtoExceptionManager) {
    this.productService = productService;
    this.categoryService = categoryService;
    this.productDtoExceptionManager = productDtoExceptionManager;
  }

  @GetMapping("/products")
  public String products(Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    List<ProductDto> productDtoList = productService.findAll();
    model.addAttribute("title", "Manage Product");
    model.addAttribute("products", productDtoList);
    model.addAttribute("size", productDtoList.size());
    return "products";
  }

  @GetMapping("/add-product")
  public String addProductForm(Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    List<Category> categories = categoryService.findAllByActivated();
    model.addAttribute("categories", categories);
    model.addAttribute("productNew", new ProductDto());
    return "add-product";
  }

  @PostMapping("/add-product")
  public String addProduct(@ModelAttribute("productNew") ProductDto productDto,
                           @RequestParam("imageProduct") MultipartFile imageProduct,
                           RedirectAttributes attributes) {
    productDtoExceptionManager.validate(productDto, imageProduct);
    if (Objects.equals(productDtoExceptionManager.getMessage(), "")) {
      productService.save(imageProduct, productDto);
      attributes.addFlashAttribute("success", "Added successfully");
    } else {
      attributes.addFlashAttribute("fail", productDtoExceptionManager.getMessage());
      return "redirect:/add-product";
    }
    return "redirect:/products";
  }

  @GetMapping("/update-product/{id}")
  public String updateProductForm(@PathVariable("id") Long id, Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    model.addAttribute("title", "Update products");
    List<Category> categories = categoryService.findAllByActivated();
    ProductDto productDto = productService.getById(id);
    model.addAttribute("categories", categories);
    model.addAttribute("productDto", productDto);
    return "update-product";
  }


  @PostMapping("/update-product/{id}")
  public String updateProduct(@ModelAttribute("productDto") ProductDto productDto,
                              @RequestParam("imageProduct")MultipartFile imageProduct,
                              RedirectAttributes attributes) {
    productDtoExceptionManager.validate(productDto, imageProduct);
    if (Objects.equals(productDtoExceptionManager.getMessage(), "")) {
      productService.update(imageProduct, productDto);
      attributes.addFlashAttribute("success", "Updated successfully");
    } else {
      attributes.addFlashAttribute("fail", productDtoExceptionManager.getMessage());
      return "redirect:/update-product/{id}";
    }
    return "redirect:/products";
  }

  @RequestMapping(value = "/enable-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
  public String enabledProduct(@PathVariable("id")Long id, RedirectAttributes attributes) {
    try {
      productService.enableById(id);
      attributes.addFlashAttribute("success", "Enabled successfully!");
    } catch (Exception e) {
      e.printStackTrace();
      attributes.addFlashAttribute("fail", "Failed to enabled!");
    }
    return "redirect:/products";
  }

  @RequestMapping(value = "/delete-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
  public String deletedProduct(@PathVariable("id") Long id, RedirectAttributes attributes) {
    try {
      productService.deleteById(id);
      attributes.addFlashAttribute("success", "Deleted successfully!");
    } catch (Exception e) {
      e.printStackTrace();
      attributes.addFlashAttribute("error", "Failed to deleted");
    }
    return "redirect:/products";
  }
}
