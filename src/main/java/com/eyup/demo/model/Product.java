package com.eyup.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generated primary key
    private Long id;

    @Column(unique = true)
    private String name;
    private double basePrice;
    private double quantity;

    @ManyToOne(fetch = FetchType.LAZY) // Many-to-one relationship with User
    @JoinColumn(name = "user_id") // This column holds the foreign key
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) // Many-to-one relationship with Tax
    @JoinColumn(name = "tax_id") // This column holds the foreign key
    private Tax tax;

    @JsonProperty("taxRate")
    public double getTaxRate() {
        if (tax != null) {
            return tax.getRate();
        }
        return 0; // Default value if tax is null
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