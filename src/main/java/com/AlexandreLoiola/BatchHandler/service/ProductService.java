package com.AlexandreLoiola.BatchHandler.service;

import com.AlexandreLoiola.BatchHandler.mapper.ProductMapper;
import com.AlexandreLoiola.BatchHandler.model.ProductModel;
import com.AlexandreLoiola.BatchHandler.repository.ProductRepository;
import com.AlexandreLoiola.BatchHandler.rest.dto.ProductDto;
import com.AlexandreLoiola.BatchHandler.rest.form.ProductForm;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final int BATCH_SIZE = 1000;

    public ProductService(ProductMapper productMapper, ProductRepository productRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    public Page<ProductDto> getAllActiveProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductModel> productPage = productRepository.findByIsActive(true, pageable);
        List<ProductDto> productDtos = productPage.getContent().stream()
                .map(productMapper::modelToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(productDtos, pageable, productPage.getTotalElements());
    }

    @Transactional
    public List<ProductDto> saveAllProducts(List<ProductForm> productForms) {
        if (productForms.isEmpty()) {
            throw new RuntimeException("Product list must not be empty");
        }
        List<List<ProductForm>> batches = createBatches(productForms, BATCH_SIZE);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Callable<List<ProductDto>>> tasks = new ArrayList<>();
        for (List<ProductForm> batch : batches) {
            tasks.add(() -> processBatch(batch));
        }
        try {
            List<Future<List<ProductDto>>> futures = executor.invokeAll(tasks);
            List<ProductDto> allProductDtos = new ArrayList<>();
            for (Future<List<ProductDto>> future : futures) {
                allProductDtos.addAll(future.get());
            }
            executor.shutdown();
            return allProductDtos;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error while processing batch products", e);
        }
    }

    private List<ProductDto> processBatch(List<ProductForm> productForms) {
        List<ProductModel> productModels = productForms.stream()
                .map(productMapper::formToModel)
                .collect(Collectors.toList());
        List<ProductModel> savedProducts = productRepository.saveAll(productModels);

        return savedProducts.stream()
                .map(productMapper::modelToDto)
                .collect(Collectors.toList());
    }

    private List<List<ProductForm>> createBatches(List<ProductForm> productForms, int batchSize) {
        List<List<ProductForm>> batches = new ArrayList<>();
        for (int i = 0; i < productForms.size(); i += batchSize) {
            batches.add(productForms.subList(i, Math.min(i + batchSize, productForms.size())));
        }
        return batches;
    }
}