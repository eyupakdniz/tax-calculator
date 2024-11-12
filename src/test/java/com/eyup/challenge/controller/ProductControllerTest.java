package com.eyup.challenge.controller;

import com.eyup.challenge.service.JwtService;
import com.eyup.challenge.service.ProductService;
import com.eyup.challenge.dto.product.ProductResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetAllProducts_Success() throws Exception {
        // Mock data
        ProductResponseDTO mockProduct = new ProductResponseDTO();
        mockProduct.setName("Test Product");
        mockProduct.setBasePrice(100.0);
        mockProduct.setQuantity(10);
        List<ProductResponseDTO> mockProductList = Collections.singletonList(mockProduct);

        // Mock behavior
        given(productService.getAllProductsForCurrentUser(Mockito.anyString())).willReturn(mockProductList);

        // Perform request and assert response
        mockMvc.perform(get("/product/get")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test Product")))
                .andExpect(jsonPath("$[0].basePrice", is(100.0)))
                .andExpect(jsonPath("$[0].quantity", is(10)));
    }

    @Test
    void testGetAllProducts_Unauthorized() throws Exception {
        // Perform request without authorization header
        mockMvc.perform(get("/product/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}

