package com.AlexandreLoiola.BatchHandler.dto.filter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePricesFilter {
    @NotBlank(message = "Category must not be blank or empty")
    private String category;

    @NotNull(message = "Percentage Increase must not be null")
    @Positive(message = "Percentage Increase must be greater than zero")
    private double percentageIncrease;
}