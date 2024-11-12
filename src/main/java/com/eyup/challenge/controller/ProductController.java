package com.eyup.challenge.controller;

import com.eyup.challenge.dto.product.CreateProductRequestDTO;
import com.eyup.challenge.dto.product.ProductResponseDTO;
import com.eyup.challenge.dto.product.UpdateProductRequestDTO;
import com.eyup.challenge.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7);
        List<ProductResponseDTO> products = productService.getAllProductsForCurrentUser(token);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/create")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody CreateProductRequestDTO productDTO, @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);

        ProductResponseDTO createdProduct = productService.createProductWithUserFromJwt(productDTO, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/update/{name}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable String name, @RequestBody UpdateProductRequestDTO product, @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7);
        ProductResponseDTO updatedProduct = productService.updateProduct(name, product, token);

        if (updatedProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String name, @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7);
        boolean deleted = productService.deleteProduct(name, token);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
