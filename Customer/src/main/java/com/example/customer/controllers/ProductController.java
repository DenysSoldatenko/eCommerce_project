package com.example.customer.controllers;

import com.example.library.dtos.CategoryDto;
import com.example.library.models.Category;
import com.example.library.models.Product;
import com.example.library.services.CategoryService;
import com.example.library.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Controller
public class ProductController {
  private final ProductService productService;

  private final CategoryService categoryService;

  @Autowired
  public ProductController(ProductService productService, CategoryService categoryService) {
    this.productService = productService;
    this.categoryService = categoryService;
  }

  @GetMapping("/products/{pageNo}")
  public String getProducts(Model model, @PathVariable("pageNo") int pageNo) {
    List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
    List<Product> products = productService.getAllProducts();
    Page<Product> listViewProducts = productService.listViewProducts(pageNo);
    model.addAttribute("categories", categoryDtoList);
    model.addAttribute("viewProducts", listViewProducts);
    model.addAttribute("totalPages", listViewProducts.getTotalPages());
    model.addAttribute("currentPage", pageNo);
    model.addAttribute("products", products);
    return "shop";
  }

  @GetMapping("/find-product/{id}")
  public String findProductById(@PathVariable("id") Long id, Model model) {
    Product product = productService.getProductById(id);
    Long categoryId = product.getCategory().getId();
    List<Product> products = productService.getRelatedProducts(categoryId);
    model.addAttribute("product", product);
    model.addAttribute("products", products);
    return "product-detail";
  }

  @GetMapping("/products-in-category/{id}")
  public String getProductsInCategory(@PathVariable("id") Long categoryId, Model model) {
    Category category = categoryService.findById(categoryId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Product not found with id: " + categoryId));
    List<CategoryDto> categories = categoryService.getCategoryAndProduct();
    List<Product> products = productService.getProductsInCategory(categoryId);
    model.addAttribute("category", category);
    model.addAttribute("categories", categories);
    model.addAttribute("products", products);
    return "products-in-category";
  }

  @GetMapping("/high-price")
  public String filterHighPrice(Model model){
    List<Category> categories = categoryService.findAllByActivated();
    List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
    List<Product> products = productService.filterHighPrice();
    model.addAttribute("categoryDtoList", categoryDtoList);
    model.addAttribute("products", products);
    model.addAttribute("categories", categories);
    return "filter-high-price";
  }

  @GetMapping("/low-price")
  public String filterLowPrice(Model model){
    List<Category> categories = categoryService.findAllByActivated();
    List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
    List<Product> products = productService.filterLowPrice();
    model.addAttribute("categoryDtoList", categoryDtoList);
    model.addAttribute("products", products);
    model.addAttribute("categories", categories);
    return "filter-low-price";
  }
}