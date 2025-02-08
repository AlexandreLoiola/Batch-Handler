package com.AlexandreLoiola.BatchHandler.controller;

import com.AlexandreLoiola.BatchHandler.dto.filter.ProductsByCategoryFilter;
import com.AlexandreLoiola.BatchHandler.dto.filter.UpdatePricesFilter;
import com.AlexandreLoiola.BatchHandler.service.ProductService;
import com.AlexandreLoiola.BatchHandler.dto.response.ProductResponse;
import com.AlexandreLoiola.BatchHandler.dto.request.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("")
    public ResponseEntity<Page<ProductResponse>> getActiveProducts(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<ProductResponse> products = productService.getAllActiveProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category")
    public ResponseEntity<Page<ProductResponse>> getProductsByCategory(
            ProductsByCategoryFilter filter,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<ProductResponse> products = productService.getAllProductsByCategory(filter, pageable);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/batch")
    public ResponseEntity<Void> saveProductsInBatch(@RequestBody List<ProductRequest> requests) {
            productService.saveAllProducts(requests);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/batch/update-prices")
    public ResponseEntity<Void> updatePricesInBatch(UpdatePricesFilter filter) {
        productService.updatePricesInBatch(filter);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
