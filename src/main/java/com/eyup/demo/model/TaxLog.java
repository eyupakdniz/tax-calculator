package com.eyup.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tax_logs")
public class TaxLog {

    @Id
    private String id;
    private String name;
    private Double basePrice;
    private Double quantity;
    private String taxName;
    private Double taxRate;
    private Double taxWithPrice;
    private Double taxAmount;
    private String userName;
}