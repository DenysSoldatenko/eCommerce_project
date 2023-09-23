package com.example.admin.controllers;

import com.example.admin.utils.ProductDtoExceptionManager;
import com.example.library.dtos.ProductDto;
import com.example.library.models.Category;
import com.example.library.models.Product;
import com.example.library.services.CategoryService;
import com.example.library.services.ProductService;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

/**
 * Controller for managing products.
 */
@Controller
public class ProductController {

  private final ProductService productService;

  private final CategoryService categoryService;

  private final ProductDtoExceptionManager productDtoExceptionManager;

  /**
   * Constructs a new ProductController with the specified services and exception manager.
   *
   * @param productService           the ProductService to be used
   * @param categoryService          the CategoryService to be used
   * @param productDtoExceptionManager the ProductDtoExceptionManager to be used
   */
  @Autowired
  public ProductController(ProductService productService,
                           CategoryService categoryService,
                           ProductDtoExceptionManager productDtoExceptionManager) {
    this.productService = productService;
    this.categoryService = categoryService;
    this.productDtoExceptionManager = productDtoExceptionManager;
  }

  /**
   * Handles the request for displaying all products.
   *
   * @param model     the model to be populated with attributes
   * @param principal the principal object representing the authenticated user
   * @return the view name for displaying products
   *     or a redirect to the login page if not authenticated
   */
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

  /**
   * Displays the form for adding a new product.
   *
   * @param model     the model to be populated with attributes
   * @param principal the principal object representing the authenticated user
   * @return the view name for adding a new product
   *     or a redirect to the login page if not authenticated
   */
  @GetMapping("/add-product")
  public String addProductForm(Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    List<Category> categories = categoryService.findActivatedCategories();
    model.addAttribute("categories", categories);
    model.addAttribute("productNew", new ProductDto());
    return "add-product";
  }

  /**
   * Adds a new product.
   *
   * @param productDto    the product DTO to add
   * @param imageProduct  the image file for the product
   * @param attributes    the redirect attributes
   * @return a redirect to the product page after adding the product
   */
  @PostMapping("/add-product")
  public String addProduct(@ModelAttribute("productNew") ProductDto productDto,
                           @RequestParam("imageProduct") MultipartFile imageProduct,
                           RedirectAttributes attributes) throws IOException {
    productDtoExceptionManager.validate(productDto, imageProduct);
    if (Objects.equals(productDtoExceptionManager.getMessage(), "")) {
      productService.createProduct(imageProduct, productDto);
      attributes.addFlashAttribute("success", "Added successfully");
    } else {
      attributes.addFlashAttribute("fail", productDtoExceptionManager.getMessage());
      return "redirect:/add-product";
    }
    return "redirect:/products/0";
  }

  /**
   * Displays the form for updating a product.
   *
   * @param id         the ID of the product to update
   * @param model      the model to be populated with attributes
   * @param principal  the principal object representing the authenticated user
   * @return the view name for updating the product
   *     or a redirect to the login page if not authenticated
   */
  @GetMapping("/update-product/{id}")
  public String updateProductForm(@PathVariable("id") Long id, Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    model.addAttribute("title", "Update products");
    List<Category> categories = categoryService.findActivatedCategories();
    ProductDto productDto = productService.findProductDetailsById(id);
    model.addAttribute("categories", categories);
    model.addAttribute("productDto", productDto);
    return "update-product";
  }


  /**
   * Updates a product.
   *
   * @param productDto   the updated product DTO
   * @param imageProduct the new image file for the product
   * @param attributes   the redirect attributes
   * @return a redirect to the product page after updating the product
   */
  @PostMapping("/update-product/{id}")
  public String updateProduct(@ModelAttribute("productDto") ProductDto productDto,
                              @RequestParam("imageProduct")MultipartFile imageProduct,
                              RedirectAttributes attributes) throws IOException {
    productDtoExceptionManager.validate(productDto, imageProduct);
    if (Objects.equals(productDtoExceptionManager.getMessage(), "")) {
      productService.updateProduct(imageProduct, productDto);
      attributes.addFlashAttribute("success", "Updated successfully");
    } else {
      attributes.addFlashAttribute("fail", productDtoExceptionManager.getMessage());
      return "redirect:/update-product/{id}";
    }
    return "redirect:/products/0";
  }

  /**
   * Handles the request for enabling a product.
   *
   * @param id         the ID of the product to enable
   * @param attributes the redirect attributes to add flash attributes
   * @return a redirect to the product page after enabling the product
   */
  @RequestMapping(value = "/enable-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
  public String enabledProduct(@PathVariable("id")Long id, RedirectAttributes attributes) {
    try {
      productService.enableProductById(id);
      attributes.addFlashAttribute("success", "Enabled successfully!");
    } catch (Exception e) {
      e.printStackTrace();
      attributes.addFlashAttribute("fail", "Failed to enabled!");
    }
    return "redirect:/products/0";
  }

  /**
   * Handles the request for deleting a product.
   *
   * @param id         the ID of the product to delete
   * @param attributes the redirect attributes to add flash attributes
   * @return a redirect to the product page after deleting the product
   */
  @RequestMapping(value = "/delete-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
  public String deletedProduct(@PathVariable("id") Long id, RedirectAttributes attributes) {
    try {
      productService.deleteProductById(id);
      attributes.addFlashAttribute("success", "Deleted successfully!");
    } catch (Exception e) {
      e.printStackTrace();
      attributes.addFlashAttribute("error", "Failed to deleted");
    }
    return "redirect:/products/0";
  }

  /**
   * Displays a specific page of products.
   *
   * @param pageNo     the page number
   * @param model      the model to be populated with attributes
   * @param principal  the principal object representing the authenticated user
   * @return the view name for displaying the product page
   *     or a redirect to the login page if not authenticated
   */
  @GetMapping("/products/{pageNo}")
  public String productPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    Page<Product> products = productService.findAllProductsPaginated(pageNo);
    model.addAttribute("title", "Manage Product");
    model.addAttribute("size", products.getSize());
    model.addAttribute("totalPages", products.getTotalPages());
    model.addAttribute("currentPage", pageNo);
    model.addAttribute("products", products);
    return "products";
  }

  /**
   * Searches for products based on a keyword.
   *
   * @param pageNo     the page number
   * @param keyword    the search keyword
   * @param model      the model to be populated with attributes
   * @param principal  the principal object representing the authenticated user
   * @return the view name for displaying the search result
   *     or a redirect to the login page if not authenticated
   */
  @GetMapping("/search-result/{pageNo}")
  public String searchProducts(@PathVariable("pageNo")int pageNo,
                               @RequestParam("keyword") String keyword,
                               Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    Page<Product> products = productService.findAllProductsPaginatedBySearch(pageNo, keyword);
    model.addAttribute("title", "Search Result");
    model.addAttribute("products", products);
    model.addAttribute("size", products.getSize());
    model.addAttribute("currentPage", pageNo);
    model.addAttribute("totalPages", products.getTotalPages());
    model.addAttribute("keyword", keyword);
    return "result-products";
  }
}
