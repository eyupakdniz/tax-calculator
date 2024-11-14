package com.eyup.demo.service;


import com.eyup.demo.dto.tax.CreateTaxRequestDTO;
import com.eyup.demo.dto.tax.TaxResponseDTO;
import com.eyup.demo.dto.tax.UpdateTaxRequestDTO;
import com.eyup.demo.model.Tax;
import com.eyup.demo.repository.TaxRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaxService {

    private final TaxRepository taxRepository;

    public List<TaxResponseDTO> getAllTaxes() {
        return taxRepository.findAll().stream()
                .map(this::convertToTaxResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<TaxResponseDTO> getTaxById(String name) {
        return taxRepository.findByName(name).map(this::convertToTaxResponseDTO);
    }

    public Tax findTaxByName(String taxName) {
        return taxRepository.findByName(taxName)
                .orElseThrow(() -> new IllegalArgumentException("Tax not found with name: " + taxName));
    }

    public TaxResponseDTO createTax(CreateTaxRequestDTO taxRequest) {
        Tax tax = Tax.builder()
                .name(taxRequest.getName())
                .rate(taxRequest.getRate())
                .build();
        return convertToTaxResponseDTO(taxRepository.save(tax));
    }


    public TaxResponseDTO updateTax(String name, UpdateTaxRequestDTO updatedTax) {
        return taxRepository.findByName(name)
                .map(tax -> {
                    tax.setName(updatedTax.getName());
                    tax.setRate(updatedTax.getRate());
                    return convertToTaxResponseDTO(taxRepository.save(tax));
                })
                .orElseThrow(() -> new RuntimeException("Tax not found: " + name));
    }

    public void deleteTax(String name) {
        Optional<Tax> tax = taxRepository.findByName(name);
        if (tax.isPresent()) {
            taxRepository.deleteById(tax.get().getId());
        }
    }

    private TaxResponseDTO convertToTaxResponseDTO(Tax tax) {
        return new TaxResponseDTO(tax.getName(), tax.getRate());
    }
}