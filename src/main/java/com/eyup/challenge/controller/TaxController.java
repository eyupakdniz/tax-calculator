package com.eyup.challenge.controller;

import com.eyup.challenge.dto.tax.CreateTaxRequestDTO;
import com.eyup.challenge.dto.tax.TaxResponseDTO;
import com.eyup.challenge.dto.tax.UpdateTaxRequestDTO;
import com.eyup.challenge.service.TaxService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tax")
@AllArgsConstructor
public class TaxController {

    private final TaxService taxService;

    @GetMapping("/get")
    public ResponseEntity<List<TaxResponseDTO>> getAllTaxes() {
        List<TaxResponseDTO> taxes = taxService.getAllTaxes();
        return new ResponseEntity<>(taxes, HttpStatus.OK);
    }

    @GetMapping("/get/{name}")
    public ResponseEntity<TaxResponseDTO> getTaxById(@PathVariable String name) {
        return taxService.getTaxById(name)
                .map(tax -> new ResponseEntity<>(tax, HttpStatus.OK)) .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); }

    @PostMapping("/create")
    public ResponseEntity<TaxResponseDTO> createTax(@RequestBody CreateTaxRequestDTO taxRequest) {
        TaxResponseDTO responseDTO = taxService.createTax(taxRequest);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update/{name}")
    public ResponseEntity<TaxResponseDTO> updateTax(@PathVariable String name, @RequestBody UpdateTaxRequestDTO tax) {
        try {
            TaxResponseDTO updatedTax = taxService.updateTax(name, tax);
            return new ResponseEntity<>(updatedTax, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Void> deleteTax(@PathVariable String name) {
        taxService.deleteTax(name);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
