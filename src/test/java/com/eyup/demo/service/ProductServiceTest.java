package com.eyup.demo.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.eyup.demo.dto.product.CreateProductRequestDTO;
import com.eyup.demo.dto.product.ProductResponseDTO;
import com.eyup.demo.dto.product.UpdateProductRequestDTO;
import com.eyup.demo.model.Product;
import com.eyup.demo.model.Tax;
import com.eyup.demo.model.User;
import com.eyup.demo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;

    @Mock
    private TaxService taxService;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    void testUpdateProduct_Success() {
        // Test veri hazırlığı
        String token = "mockToken";
        String productName = "Product1";
        UpdateProductRequestDTO updateRequest = new UpdateProductRequestDTO();
        updateRequest.setName("UpdatedProduct");
        updateRequest.setBasePrice(200.0);
        updateRequest.setQuantity(20.0);
        updateRequest.setTaxName("StandardTax");

        User mockUser = new User();
        mockUser.setId(1L);

        Tax mockTax = new Tax();
        mockTax.setName("StandardTax");

        Product mockProduct = new Product();
        mockProduct.setName(productName);
        mockProduct.setBasePrice(100.0);
        mockProduct.setQuantity(10);
        mockProduct.setUser(mockUser);

        // Mock davranışları
        when(userService.getCurrentUserFromJwt(token)).thenReturn(mockUser);
        when(taxService.findTaxByName(updateRequest.getTaxName())).thenReturn(mockTax);
        when(productRepository.findByName(productName)).thenReturn(Optional.of(mockProduct));
        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

        // Methodu çağır ve doğrula
        ProductResponseDTO responseDTO = productService.updateProduct(productName, updateRequest, token);

        assertNotNull(responseDTO);
        assertEquals("UpdatedProduct", responseDTO.getName());
        assertEquals(200.0, responseDTO.getBasePrice());
        assertEquals(20, responseDTO.getQuantity());
        verify(productRepository, times(1)).save(mockProduct);
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        String token = "mockToken";
        String productName = "NonExistentProduct";
        UpdateProductRequestDTO updateRequest = new UpdateProductRequestDTO();
        updateRequest.setName("UpdatedProduct");
        updateRequest.setBasePrice(200.0);
        updateRequest.setQuantity(20.0);
        updateRequest.setTaxName("StandardTax");

        User mockUser = new User();
        mockUser.setId(1L);

        when(userService.getCurrentUserFromJwt(token)).thenReturn(mockUser);
        when(productRepository.findByName(productName)).thenReturn(Optional.empty());

        ProductResponseDTO responseDTO = productService.updateProduct(productName, updateRequest, token);

        assertNull(responseDTO);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_UserNotAuthorized() {
        String token = "mockToken";
        String productName = "Product1";
        UpdateProductRequestDTO updateRequest = new UpdateProductRequestDTO();
        updateRequest.setName("UpdatedProduct");
        updateRequest.setBasePrice(200.0);
        updateRequest.setQuantity(20.0);
        updateRequest.setTaxName("StandardTax");

        User mockUser = new User();
        mockUser.setId(1L);

        User unauthorizedUser = new User();
        unauthorizedUser.setId(1L);

        Product mockProduct = new Product();
        mockProduct.setName(productName);
        mockProduct.setBasePrice(100.0);
        mockProduct.setQuantity(10);
        mockProduct.setUser(unauthorizedUser);

        when(userService.getCurrentUserFromJwt(token)).thenReturn(mockUser);
        when(productRepository.findByName(productName)).thenReturn(Optional.of(mockProduct));

        ProductResponseDTO responseDTO = productService.updateProduct(productName, updateRequest, token);

        assertNull(responseDTO);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testDeleteProduct_Success() {
        String token = "mockToken";
        String productName = "Product1";

        User mockUser = new User();
        mockUser.setId(1L);

        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName(productName);
        mockProduct.setUser(mockUser);

        // Mock davranışları
        when(userService.getCurrentUserFromJwt(token)).thenReturn(mockUser);
        when(productRepository.findByName(productName)).thenReturn(Optional.of(mockProduct));

        // Methodu çağır ve doğrula
        boolean result = productService.deleteProduct(productName, token);

        assertTrue(result);
        verify(productRepository, times(1)).delete(mockProduct);
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        String token = "mockToken";
        String productName = "NonExistentProduct";

        User mockUser = new User();
        mockUser.setId(1L);

        when(userService.getCurrentUserFromJwt(token)).thenReturn(mockUser);
        when(productRepository.findByName(productName)).thenReturn(Optional.empty());

        // Methodu çağır ve doğrula
        boolean result = productService.deleteProduct(productName, token);

        assertFalse(result);
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void testDeleteProduct_UserNotAuthorized() {
        String token = "mockToken";
        String productName = "Product1";

        User mockUser = new User();
        mockUser.setId(1L);

        User unauthorizedUser = new User();
        unauthorizedUser.setId(1L);

        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName(productName);
        mockProduct.setUser(unauthorizedUser);

        when(userService.getCurrentUserFromJwt(token)).thenReturn(mockUser);
        when(productRepository.findByName(productName)).thenReturn(Optional.of(mockProduct));

        // Methodu çağır ve doğrula
        boolean result = productService.deleteProduct(productName, token);

        assertFalse(result);
        verify(productRepository, never()).delete(any(Product.class));
    }
}