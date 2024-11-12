package com.eyup.challenge.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private double basePrice;
    private double quantity;

    @DBRef
    private User user;

    @DBRef
    private Tax tax;

    @JsonProperty("taxRate")
    public double getTaxRate() {
        if (tax != null) {
            return tax.getRate();
        }
        return 0;
    }

    @JsonProperty("taxWithPrice")
    public double getTaxWithPrice() {
        double rate = getTaxRate();
        return basePrice * (1 + rate / 100);
    }

    @JsonProperty("taxAmount")
    public double getTaxAmount() {
        double rate = getTaxRate();
        return basePrice * rate / 100;
    }

    @JsonProperty("taxName")
    public String getTaxName() {
        if (tax != null) {
            return tax.getName();
        }
        return null;
    }

    @JsonProperty("userName")
    public String getUserName() {
        if (user != null) {
            return user.getUsername();
        }
        return null;
    }
}