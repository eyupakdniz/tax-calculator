package com.eyup.demo.service;

import com.eyup.demo.dto.tax.CreateTaxRequestDTO;
import com.eyup.demo.dto.tax.TaxResponseDTO;
import com.eyup.demo.dto.tax.UpdateTaxRequestDTO;
import com.eyup.demo.model.Tax;
import com.eyup.demo.repository.TaxRepository;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

class TaxServiceTest {

    private TaxRepository taxRepository;

    private TaxService taxService;

    @BeforeEach
    void setUp() {

        taxRepository = Mockito.mock(TaxRepository.class);
        taxService = new TaxService(taxRepository);
    }

    @Test
    void testGetAllTaxes() {
        // Arrange
        Tax tax1 = new Tax("Tax1", 0.18);
        Tax tax2 = new Tax("Tax2", 0.15);
        when(taxRepository.findAll()).thenReturn(List.of(tax1, tax2));

        // Act
        List<TaxResponseDTO> taxResponseDTOList = taxService.getAllTaxes();

        // Assert
        assertEquals(2, taxResponseDTOList.size());
        assertEquals("Tax1", taxResponseDTOList.get(0).getName());
        assertEquals(0.18, taxResponseDTOList.get(0).getRate());
        assertEquals("Tax2", taxResponseDTOList.get(1).getName());
        assertEquals(0.15, taxResponseDTOList.get(1).getRate());
    }

    @Test
    void testGetAllTaxesEmptyList() {
        // Arrange
        when(taxRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<TaxResponseDTO> taxResponseDTOList = taxService.getAllTaxes();

        // Assert
        assertTrue(taxResponseDTOList.isEmpty());
    }

    @Test
    void testGetTaxById() {
        // Arrange
        Tax tax = new Tax("Tax1", 10.0);  // Expected Tax object
        when(taxRepository.findByName("Tax1")).thenReturn(Optional.of(tax));  // Mock repository

        // Act
        Optional<TaxResponseDTO> result = taxService.getTaxById("Tax1");

        // Assert
        assertTrue(result.isPresent());  // Check if result is present
        assertEquals("Tax1", result.get().getName());  // Check that the name is correct
        assertEquals(10.0, result.get().getRate());  // Check that the rate is correct
    }


    @Test
    void testGetTaxByIdNotFound() {
        // Arrange
        when(taxRepository.findByName("NonExistentTax")).thenReturn(Optional.empty());

        // Act
        Optional<TaxResponseDTO> taxResponseDTO = taxService.getTaxById("NonExistentTax");

        // Assert
        assertFalse(taxResponseDTO.isPresent());
    }

    @Test
    void testCreateTax() {
        // Arrange
        CreateTaxRequestDTO taxRequest = new CreateTaxRequestDTO("Tax1", 10.0);
        Tax tax = new Tax("Tax1", 10.0);  // Expected tax
        when(taxRepository.save(any(Tax.class))).thenReturn(tax);  // Mock save to return the same tax

        // Act
        TaxResponseDTO result = taxService.createTax(taxRequest);

        // Assert
        assertNotNull(result);  // Ensure the result is not null
        assertEquals("Tax1", result.getName());  // Check that the name is correct
        assertEquals(10.0, result.getRate());  // Check that the rate is correct
    }


    @Test
    void testUpdateTax() {
        // Arrange
        UpdateTaxRequestDTO updateRequest = new UpdateTaxRequestDTO("UpdatedTax", 0.20);
        Tax tax = new Tax("Tax1", 0.18);
        when(taxRepository.findByName("Tax1")).thenReturn(Optional.of(tax));
        when(taxRepository.save(any(Tax.class))).thenReturn(tax);

        // Act
        TaxResponseDTO updatedTax = taxService.updateTax("Tax1", updateRequest);

        // Assert
        assertEquals("UpdatedTax", updatedTax.getName());
        assertEquals(0.20, updatedTax.getRate());
    }

    @Test
    void testUpdateTaxNotFound() {
        // Arrange
        UpdateTaxRequestDTO updateRequest = new UpdateTaxRequestDTO("UpdatedTax", 0.20);
        when(taxRepository.findByName("NonExistentTax")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> taxService.updateTax("NonExistentTax", updateRequest));
    }

    @Test
    void testDeleteTax() {
        // Arrange
        Tax tax = new Tax("Tax1", 0.18);
        when(taxRepository.findByName("Tax1")).thenReturn(Optional.of(tax));

        // Act
        taxService.deleteTax("Tax1");

        // Assert
        verify(taxRepository, times(1)).deleteById(tax.getId());
    }

    @Test
    void testDeleteTaxNotFound() {
        // Arrange
        when(taxRepository.findByName("NonExistentTax")).thenReturn(Optional.empty());

        // Act & Assert
        assertDoesNotThrow(() -> taxService.deleteTax("NonExistentTax"));

        // `deleteById`'nin çağrılmaması gerektiğini kontrol et
        verify(taxRepository, times(0)).deleteById(any());  // deleteById çağrılmamalıdır
    }



}