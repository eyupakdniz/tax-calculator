package com.eyup.challenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.eyup.challenge.dto.product.CreateProductRequestDTO;
import com.eyup.challenge.dto.product.ProductResponseDTO;
import com.eyup.challenge.dto.product.UpdateProductRequestDTO;
import com.eyup.challenge.model.Product;
import com.eyup.challenge.model.Tax;
import com.eyup.challenge.model.User;
import com.eyup.challenge.repository.ProductRepository;
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
    void testGetAllProductsForCurrentUser() {
        // Mocking dependencies
        String token = "mockToken";
        User mockUser = new User();
        when(userService.getCurrentUserFromJwt(token)).thenReturn(mockUser);

        Product product = new Product();
        when(productRepository.findByUser(mockUser)).thenReturn(Collections.singletonList(product));

        // Execute service call
        List<ProductResponseDTO> result = productService.getAllProductsForCurrentUser(token);

        // Validate results
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCreateProductWithUserFromJwt() {
        // Mocking dependencies
        String token = "mockToken";
        User mockUser = new User();
        when(userService.getCurrentUserFromJwt(token)).thenReturn(mockUser);

        Tax mockTax = new Tax();
        when(taxService.findTaxByName("TaxName")).thenReturn(mockTax);

        CreateProductRequestDTO request = new CreateProductRequestDTO();
        request.setName("ProductName");
        request.setBasePrice(100.0);
        request.setQuantity(10);
        request.setTaxName("TaxName");

        Product mockProduct = new Product();
        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

        // Execute service call
        ProductResponseDTO result = productService.createProductWithUserFromJwt(request, token);

        // Validate results
        assertNotNull(result);
    }

    @Test
    void testUpdateProduct_Success() {
        // Test veri hazırlığı
        String token = "mockToken";
        String productName = "Product1";
        UpdateProductRequestDTO updateRequest = new UpdateProductRequestDTO();
        updateRequest.setName("UpdatedProduct");
        updateRequest.setBasePrice(200.0);
        updateRequest.setQuantity(20);
        updateRequest.setTaxName("StandardTax");

        User mockUser = new User();
        mockUser.setId(String.valueOf(1L));

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
        updateRequest.setQuantity(20);
        updateRequest.setTaxName("StandardTax");

        User mockUser = new User();
        mockUser.setId(String.valueOf(1L));

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
        updateRequest.setQuantity(20);
        updateRequest.setTaxName("StandardTax");

        User mockUser = new User();
        mockUser.setId(String.valueOf(1L));

        User unauthorizedUser = new User();
        unauthorizedUser.setId(String.valueOf(2L));

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
        mockUser.setId(String.valueOf(1L));

        Product mockProduct = new Product();
        mockProduct.setId(String.valueOf(1L));
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
        mockUser.setId(String.valueOf(1L));

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
        mockUser.setId(String.valueOf(1L));

        User unauthorizedUser = new User();
        unauthorizedUser.setId(String.valueOf(2L));

        Product mockProduct = new Product();
        mockProduct.setId(String.valueOf(1L));
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

