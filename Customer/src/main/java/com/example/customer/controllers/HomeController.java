package com.example.customer.controllers;

import com.example.library.dtos.ProductDto;
import com.example.library.models.Category;
import com.example.library.models.Product;
import com.example.library.services.CategoryService;
import com.example.library.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final ProductService productService;

    private final CategoryService categoryService;

    @Autowired
    public HomeController(ProductService productService,
                          CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/menu")
    public String index(Model model) {
        List<Category> categories = categoryService.findAll();
        List<ProductDto> productDtoList = productService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("products", productDtoList);
        return "index";
    }

    @GetMapping("/products")
    public String products(Model model) {
        List<Product> listViewProduct = productService.listViewProduct();
        List<Product> productList = productService.getAllProducts();
        model.addAttribute("productViews", listViewProduct);
        model.addAttribute("products", productList);
        return "products";
    }
}