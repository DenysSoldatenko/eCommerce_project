package com.example.customer.controllers;

import com.example.customer.utils.SessionAttributeSetter;
import com.example.library.dtos.CategoryDto;
import com.example.library.models.Category;
import com.example.library.models.Product;
import com.example.library.services.CategoryService;
import com.example.library.services.ProductService;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller class for handling product-related operations.
 */
@Controller
public class ProductController {
  private final ProductService productService;

  private final CategoryService categoryService;

  private final SessionAttributeSetter sessionAttributeSetter;

  @Autowired
  public ProductController(ProductService productService, CategoryService categoryService,
                           SessionAttributeSetter sessionAttributeSetter) {
    this.productService = productService;
    this.categoryService = categoryService;
    this.sessionAttributeSetter = sessionAttributeSetter;
  }

  /**
   * Retrieves and displays a page of products.
   *
   * @param model   the model to be populated with attributes
   * @param pageNo  the page number
   * @return the view name for displaying the product page
   */
  @GetMapping("/products/{pageNo}")
  public String getProducts(Principal principal, HttpSession session,
                            Model model, @PathVariable("pageNo") int pageNo) {
    sessionAttributeSetter.setSessionAttributes(principal, session);
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

  /**
   * Retrieves and displays a specific product by ID.
   *
   * @param id     the ID of the product to retrieve
   * @param model  the model to be populated with attributes
   * @return the view name for displaying the product detail page
   */
  @GetMapping("/find-product/{id}")
  public String findProductById(Principal principal, HttpSession session,
                                @PathVariable("id") Long id, Model model) {
    sessionAttributeSetter.setSessionAttributes(principal, session);
    Product product = productService.getProductById(id);
    Long categoryId = product.getCategory().getId();
    List<Product> products = productService.getRelatedProducts(categoryId);
    model.addAttribute("product", product);
    model.addAttribute("products", products);
    return "find-product";
  }

  /**
   * Retrieves and displays products in a specific category.
   *
   * @param categoryId  the ID of the category
   * @param model       the model to be populated with attributes
   * @return the view name for displaying the products in category page
   * @throws ResponseStatusException if the category is not found
   */
  @GetMapping("/products-in-category/{id}")
  public String getProductsInCategory(Principal principal, HttpSession session,
                                      @PathVariable("id") Long categoryId, Model model) {
    Category category = categoryService.findById(categoryId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Category not found with id: " + categoryId));
    sessionAttributeSetter.setSessionAttributes(principal, session);
    List<CategoryDto> categories = categoryService.getCategoryAndProduct();
    List<Product> products = productService.getProductsInCategory(categoryId);
    model.addAttribute("category", category);
    model.addAttribute("categories", categories);
    model.addAttribute("products", products);
    return "products-in-category";
  }

  /**
   * Filters and displays products with high prices.
   *
   * @param model  the model to be populated with attributes
   * @return the view name for displaying the filtered high-price products
   */
  @GetMapping("/high-price")
  public String filterHighPrice(Principal principal, HttpSession session, Model model) {
    sessionAttributeSetter.setSessionAttributes(principal, session);
    List<Category> categories = categoryService.findAllByActivated();
    List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
    List<Product> products = productService.filterHighPrice();
    model.addAttribute("categoryDtoList", categoryDtoList);
    model.addAttribute("products", products);
    model.addAttribute("categories", categories);
    return "filter-high-price";
  }

  /**
   * Filters and displays products with low prices.
   *
   * @param model  the model to be populated with attributes
   * @return the view name for displaying the filtered low-price products
   */
  @GetMapping("/low-price")
  public String filterLowPrice(Principal principal, HttpSession session, Model model) {
    sessionAttributeSetter.setSessionAttributes(principal, session);
    List<Category> categories = categoryService.findAllByActivated();
    List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
    List<Product> products = productService.filterLowPrice();
    model.addAttribute("categoryDtoList", categoryDtoList);
    model.addAttribute("products", products);
    model.addAttribute("categories", categories);
    return "filter-low-price";
  }

  /**
   * Handles the request for searching products based on the provided keyword.
   *
   * @param keyword  the search keyword
   * @param model    the model to be populated with attributes
   * @return the view name for displaying the search results
   */
  @GetMapping("/search-product")
  public String searchProduct(Principal principal, HttpSession session,
                              @RequestParam("keyword") String keyword, Model model) {
    sessionAttributeSetter.setSessionAttributes(principal, session);
    List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
    List<Product> filteredProducts = productService.searchProducts(keyword);
    List<Category> filteredCategories = categoryService.getFilteredCategories(filteredProducts);
    model.addAttribute("categoryDtoList", categoryDtoList);
    model.addAttribute("products", filteredProducts);
    model.addAttribute("categories", filteredCategories);
    model.addAttribute("keyword", keyword);
    return "search-product";
  }
}