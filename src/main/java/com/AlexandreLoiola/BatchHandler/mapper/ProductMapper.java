package com.AlexandreLoiola.BatchHandler.mapper;

import com.AlexandreLoiola.BatchHandler.model.ProductModel;
import com.AlexandreLoiola.BatchHandler.rest.dto.ProductDto;
import com.AlexandreLoiola.BatchHandler.rest.form.ProductForm;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductModel formToModel(ProductForm productForm) {
        return ProductModel.builder()
                .name(productForm.getName())
                .description(productForm.getDescription())
                .price(productForm.getPrice())
                .quantity(productForm.getQuantity())
                .build();
    }

    public ProductDto modelToDto(ProductModel productModel) {
        ProductDto productDto = new ProductDto();
        productDto.setName(productModel.getName());
        productDto.setDescription(productModel.getDescription());
        productDto.setPrice(productModel.getPrice());
        productDto.setQuantity(productModel.getQuantity());
        productDto.setIsActive(productModel.getIsActive());
        productDto.setCreatedAt(productModel.getCreatedAt());
        productDto.setUpdatedAt(productModel.getUpdatedAt());

        return productDto;
    }
}