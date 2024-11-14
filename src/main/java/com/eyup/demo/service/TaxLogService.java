package com.eyup.demo.service;

import com.eyup.demo.dto.product.ProductResponseDTO;
import com.eyup.demo.model.TaxLog;
import com.eyup.demo.repository.TaxLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaxLogService {

    private final TaxLogRepository taxLogRepository;

    public void logTaxCalculation(ProductResponseDTO productResponseDTO) {
        if (productResponseDTO != null) {
            Double basePrice = productResponseDTO.getBasePrice();
            Double quantity = productResponseDTO.getQuantity();
            String taxName = productResponseDTO.getTaxName();
            Double taxRate = productResponseDTO.getTaxRate();
            Double taxWithPrice = productResponseDTO.getTaxWithPrice();
            Double taxAmount = productResponseDTO.getTaxAmount();
            String userName = productResponseDTO.getUserName();

            TaxLog taxLog = TaxLog.builder()
                    .name("Tax Calculation Log")
                    .basePrice(basePrice)
                    .quantity(quantity)
                    .taxName(taxName)
                    .taxRate(taxRate)
                    .taxWithPrice(taxWithPrice)
                    .taxAmount(taxAmount)
                    .userName(userName)
                    .build();

            taxLogRepository.save(taxLog);
        }
    }
}