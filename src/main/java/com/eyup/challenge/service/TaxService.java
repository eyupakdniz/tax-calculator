package com.eyup.challenge.service;

import com.eyup.challenge.dto.tax.CreateTaxRequestDTO;
import com.eyup.challenge.dto.tax.TaxResponseDTO;
import com.eyup.challenge.dto.tax.UpdateTaxRequestDTO;
import com.eyup.challenge.model.Tax;
import com.eyup.challenge.repository.TaxRepository;
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

    public Optional<TaxResponseDTO> getTaxById(String id) {
        return taxRepository.findById(id).map(this::convertToTaxResponseDTO);
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


    public TaxResponseDTO updateTax(String id, UpdateTaxRequestDTO updatedTax) {
        return taxRepository.findById(id)
                .map(tax -> {
                    tax.setName(updatedTax.getName());
                    tax.setRate(updatedTax.getRate());
                    return convertToTaxResponseDTO(taxRepository.save(tax));
                })
                .orElseThrow(() -> new RuntimeException("Tax not found: " + id));
    }

    public void deleteTax(String id) {
        taxRepository.deleteById(id);
    }

    private TaxResponseDTO convertToTaxResponseDTO(Tax tax) {
        return new TaxResponseDTO(tax.getName(), tax.getRate());
    }
}
