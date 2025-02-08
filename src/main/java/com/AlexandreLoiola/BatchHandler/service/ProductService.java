package com.AlexandreLoiola.BatchHandler.service;

import com.AlexandreLoiola.BatchHandler.dto.filter.ProductsByCategoryFilter;
import com.AlexandreLoiola.BatchHandler.dto.filter.UpdatePricesFilter;
import com.AlexandreLoiola.BatchHandler.mapper.ProductMapper;
import com.AlexandreLoiola.BatchHandler.model.ProductModel;
import com.AlexandreLoiola.BatchHandler.repository.ProductRepository;
import com.AlexandreLoiola.BatchHandler.dto.response.ProductResponse;
import com.AlexandreLoiola.BatchHandler.dto.request.ProductRequest;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final int BATCH_SIZE = 1000;

    public ProductService(ProductMapper productMapper, ProductRepository productRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    public Page<ProductResponse> getAllActiveProducts(Pageable pageable) {
        Page<ProductModel> productPage = productRepository.findByIsActive(true, pageable);
        List<ProductResponse> response = productMapper.modelsToDtos(productPage.getContent());
        return new PageImpl<>(response, pageable, productPage.getTotalElements());
    }

    public Page<ProductResponse> getAllProductsByCategory(ProductsByCategoryFilter filter, Pageable pageable) {
        Page<ProductModel> productPage = productRepository.findByCategory(filter.getCategory(), pageable);
        List<ProductResponse> response = productMapper.modelsToDtos(productPage.getContent());
        return new PageImpl<>(response, pageable, productPage.getTotalElements());
    }

    @Transactional
    public void saveAllProducts(List<ProductRequest> productRequests) {
        if (productRequests.isEmpty()) {
            throw new RuntimeException("Product list must not be empty");
        }
        List<List<ProductRequest>> batches = createBatches(productRequests, BATCH_SIZE);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Callable<Void>> tasks = new ArrayList<>();
        for (List<ProductRequest> batch : batches) {
            tasks.add(() -> {
                processBatch(batch);
                return null;
            });
        }
        try {
            executor.invokeAll(tasks);
            executor.shutdown();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error while processing batch products", e);
        }
    }

    @Transactional
    public void updatePricesInBatch(UpdatePricesFilter filter) {
        int page = 0;
        Page<ProductModel> products;
        do {
            products = productRepository.findByCategory(filter.getCategory(), PageRequest.of(page, BATCH_SIZE));
            products.forEach(product -> {
                BigDecimal currentPrice = product.getPrice();
                BigDecimal increase = currentPrice.multiply(BigDecimal.valueOf(filter.getPercentageIncrease() / 100));
                product.setPrice(currentPrice.add(increase));
            });
            productRepository.saveAll(products);
            page++;
        } while (!products.isEmpty());
    }

    private void processBatch(List<ProductRequest> productRequests) {
        List<ProductModel> productModels = productRequests.stream()
                .map(productMapper::formToModel)
                .collect(Collectors.toList());
        productRepository.saveAll(productModels);
    }

    private List<List<ProductRequest>> createBatches(List<ProductRequest> productRequests, int batchSize) {
        List<List<ProductRequest>> batches = new ArrayList<>();
        for (int i = 0; i < productRequests.size(); i += batchSize) {
            batches.add(productRequests.subList(i, Math.min(i + batchSize, productRequests.size())));
        }
        return batches;
    }
}