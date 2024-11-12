package com.eyup.challenge.dto.product;

import com.eyup.challenge.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductResponseDTO {

    private String name;
    private double basePrice;
    private double quantity;
    private String taxName;
    private double taxRate;
    private double taxWithPrice;
    private double taxAmount;
    private String userName;

    public ProductResponseDTO(Product product) {
        if (product != null) {
            this.name = product.getName();
            this.basePrice = product.getBasePrice();
            this.quantity = product.getQuantity();
            this.taxName = (product.getTax() != null) ? product.getTax().getName() : null;
            this.taxRate = product.getTax() != null ? product.getTax().getRate() : 0.0;
            this.taxWithPrice = product.getTaxWithPrice();
            this.taxAmount = product.getTaxAmount();
            this.userName = (product.getUser() != null) ? product.getUser().getUsername() : null;
        }
    }




}
