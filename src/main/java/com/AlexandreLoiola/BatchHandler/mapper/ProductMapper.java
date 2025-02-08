package com.AlexandreLoiola.BatchHandler.mapper;

import com.AlexandreLoiola.BatchHandler.model.ProductModel;
import com.AlexandreLoiola.BatchHandler.dto.response.ProductResponse;
import com.AlexandreLoiola.BatchHandler.dto.request.ProductRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    public ProductModel formToModel(ProductRequest productRequest) {
        return ProductModel.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .category(productRequest.getCategory())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .build();
    }

    public ProductResponse modelToDto(ProductModel productModel) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setName(productModel.getName());
        productResponse.setDescription(productModel.getDescription());
        productResponse.setCategory(productModel.getCategory());
        productResponse.setPrice(productModel.getPrice());
        productResponse.setQuantity(productModel.getQuantity());
        productResponse.setIsActive(productModel.getIsActive());
        productResponse.setCreatedAt(productModel.getCreatedAt());
        productResponse.setUpdatedAt(productModel.getUpdatedAt());

        return productResponse;
    }

    public List<ProductResponse> modelsToDtos(List<ProductModel> productModels) {
        return productModels.stream()
                .map(this::modelToDto)
                .collect(Collectors.toList());
    }
}