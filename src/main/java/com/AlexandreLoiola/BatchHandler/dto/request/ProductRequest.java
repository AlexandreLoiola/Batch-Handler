package com.AlexandreLoiola.BatchHandler.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Product name must not be empty")
    @Size(min = 1, max = 100, message = "Product name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Description must not be blank or empty")
    @Size(min = 1, max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @NotBlank(message = "Category must not be blank or empty")
    private String category;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    @NotNull(message = "Quantity must not be null")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;
}