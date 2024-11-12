package com.eyup.challenge.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateProductRequestDTO {
    private String name;
    private double basePrice;
    private double quantity;
    private String taxName;

}
