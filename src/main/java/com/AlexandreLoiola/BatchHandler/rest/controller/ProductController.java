package com.AlexandreLoiola.BatchHandler.rest.controller;

import com.AlexandreLoiola.BatchHandler.service.ProductService;
import com.AlexandreLoiola.BatchHandler.rest.dto.ProductDto;
import com.AlexandreLoiola.BatchHandler.rest.form.ProductForm;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/active")
    public ResponseEntity<Page<ProductDto>> getActiveProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Page<ProductDto> products = productService.getAllActiveProducts(page, size);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/batch/create")
    public ResponseEntity<List<ProductDto>> saveProductsInBatch(@RequestBody List<ProductForm> productForms) {
            List<ProductDto> savedProducts = productService.saveAllProducts(productForms);
            return new ResponseEntity<>(savedProducts, HttpStatus.CREATED);
    }
}
