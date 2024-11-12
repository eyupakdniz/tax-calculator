package com.eyup.challenge.controller;

import com.eyup.challenge.dto.product.CreateProductRequestDTO;
import com.eyup.challenge.dto.product.ProductResponseDTO;
import com.eyup.challenge.dto.product.UpdateProductRequestDTO;
import com.eyup.challenge.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProducts_withValidToken_shouldReturnProductList() {
        String token = "validToken";
        ProductResponseDTO product = new ProductResponseDTO();
        when(productService.getAllProductsForCurrentUser(token)).thenReturn(Collections.singletonList(product));

        ResponseEntity<List<ProductResponseDTO>> response = productController.getAllProducts("Bearer " + token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAllProducts_withInvalidToken_shouldReturnUnauthorized() {
        ResponseEntity<List<ProductResponseDTO>> response = productController.getAllProducts(null);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void createProduct_withValidToken_shouldReturnCreatedProduct() {
        String token = "validToken";
        CreateProductRequestDTO productDTO = new CreateProductRequestDTO();
        ProductResponseDTO createdProduct = new ProductResponseDTO();
        when(productService.createProductWithUserFromJwt(productDTO, token)).thenReturn(createdProduct);

        ResponseEntity<ProductResponseDTO> response = productController.createProduct(productDTO, "Bearer " + token);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdProduct, response.getBody());
    }

    @Test
    void createProduct_withInvalidToken_shouldReturnUnauthorized() {
        CreateProductRequestDTO productDTO = new CreateProductRequestDTO();

        ResponseEntity<ProductResponseDTO> response = productController.createProduct(productDTO, null);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void updateProduct_withValidTokenAndProductExists_shouldReturnUpdatedProduct() {
        String token = "validToken";
        String productName = "testProduct";
        UpdateProductRequestDTO updateProductDTO = new UpdateProductRequestDTO();
        ProductResponseDTO updatedProduct = new ProductResponseDTO();
        when(productService.updateProduct(productName, updateProductDTO, token)).thenReturn(updatedProduct);

        ResponseEntity<ProductResponseDTO> response = productController.updateProduct(productName, updateProductDTO, "Bearer " + token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProduct, response.getBody());
    }

    @Test
    void updateProduct_withNonexistentProduct_shouldReturnNotFound() {
        String token = "validToken";
        String productName = "nonexistentProduct";
        UpdateProductRequestDTO updateProductDTO = new UpdateProductRequestDTO();
        when(productService.updateProduct(productName, updateProductDTO, token)).thenReturn(null);

        ResponseEntity<ProductResponseDTO> response = productController.updateProduct(productName, updateProductDTO, "Bearer " + token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteProduct_withValidTokenAndProductExists_shouldReturnNoContent() {
        String token = "validToken";
        String productName = "testProduct";
        when(productService.deleteProduct(productName, token)).thenReturn(true);

        ResponseEntity<Void> response = productController.deleteProduct(productName, "Bearer " + token);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteProduct_withNonexistentProduct_shouldReturnNotFound() {
        String token = "validToken";
        String productName = "nonexistentProduct";
        when(productService.deleteProduct(productName, token)).thenReturn(false);

        ResponseEntity<Void> response = productController.deleteProduct(productName, "Bearer " + token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
