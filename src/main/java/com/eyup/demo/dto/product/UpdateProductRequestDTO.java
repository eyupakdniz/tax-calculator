package com.eyup.demo.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateProductRequestDTO {
    private String name;
    private Double basePrice;
    private Double quantity;
    private String taxName;
}
