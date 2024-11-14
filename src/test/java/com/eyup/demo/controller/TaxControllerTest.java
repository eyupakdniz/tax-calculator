package com.eyup.demo.controller;

import com.eyup.demo.dto.tax.CreateTaxRequestDTO;
import com.eyup.demo.dto.tax.TaxResponseDTO;
import com.eyup.demo.dto.tax.UpdateTaxRequestDTO;
import com.eyup.demo.service.TaxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TaxControllerTest {

    @Mock
    private TaxService taxService;

    @InjectMocks
    private TaxController taxController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTaxes_shouldReturnListOfTaxes() {
        TaxResponseDTO tax = new TaxResponseDTO();
        when(taxService.getAllTaxes()).thenReturn(Collections.singletonList(tax));

        ResponseEntity<List<TaxResponseDTO>> response = taxController.getAllTaxes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAllTaxes_whenNoTaxes_shouldReturnEmptyList() {
        when(taxService.getAllTaxes()).thenReturn(Collections.emptyList());

        ResponseEntity<List<TaxResponseDTO>> response = taxController.getAllTaxes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void getTaxById_withValidName_shouldReturnTax() {
        String name = "testTax";
        TaxResponseDTO tax = new TaxResponseDTO();
        when(taxService.getTaxById(name)).thenReturn(Optional.of(tax));

        ResponseEntity<TaxResponseDTO> response = taxController.getTaxById(name);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tax, response.getBody());
    }

    @Test
    void getTaxById_withInvalidName_shouldReturnNotFound() {
        String name = "nonexistentTax";
        when(taxService.getTaxById(name)).thenReturn(Optional.empty());

        ResponseEntity<TaxResponseDTO> response = taxController.getTaxById(name);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createTax_shouldReturnCreatedTax() {
        CreateTaxRequestDTO createTaxRequest = new CreateTaxRequestDTO();
        TaxResponseDTO createdTax = new TaxResponseDTO();
        when(taxService.createTax(createTaxRequest)).thenReturn(createdTax);

        ResponseEntity<TaxResponseDTO> response = taxController.createTax(createTaxRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdTax, response.getBody());
    }

    @Test
    void createTax_whenServiceThrowsException_shouldReturnBadRequest() {
        CreateTaxRequestDTO createTaxRequest = new CreateTaxRequestDTO();
        when(taxService.createTax(createTaxRequest)).thenThrow(new RuntimeException("Error creating tax"));

        assertThrows(RuntimeException.class, () -> taxController.createTax(createTaxRequest));
    }

    @Test
    void updateTax_withValidName_shouldReturnUpdatedTax() {
        String name = "testTax";
        UpdateTaxRequestDTO updateTaxRequest = new UpdateTaxRequestDTO();
        TaxResponseDTO updatedTax = new TaxResponseDTO();
        when(taxService.updateTax(name, updateTaxRequest)).thenReturn(updatedTax);

        ResponseEntity<TaxResponseDTO> response = taxController.updateTax(name, updateTaxRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTax, response.getBody());
    }

    @Test
    void updateTax_withInvalidName_shouldReturnNotFound() {
        String name = "nonexistentTax";
        UpdateTaxRequestDTO updateTaxRequest = new UpdateTaxRequestDTO();
        when(taxService.updateTax(name, updateTaxRequest)).thenThrow(new RuntimeException("Tax not found"));

        ResponseEntity<TaxResponseDTO> response = taxController.updateTax(name, updateTaxRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteTax_withValidName_shouldReturnNoContent() {
        String name = "testTax";

        ResponseEntity<Void> response = taxController.deleteTax(name);

        verify(taxService, times(1)).deleteTax(name);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteTax_withInvalidName_shouldStillReturnNoContent() {
        String name = "nonexistentTax";

        ResponseEntity<Void> response = taxController.deleteTax(name);

        verify(taxService, times(1)).deleteTax(name);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}