package com.eyup.demo.service;

import com.eyup.demo.dto.product.CreateProductRequestDTO;
import com.eyup.demo.dto.product.ProductResponseDTO;
import com.eyup.demo.dto.product.UpdateProductRequestDTO;
import com.eyup.demo.model.Product;
import com.eyup.demo.model.Tax;
import com.eyup.demo.model.User;
import com.eyup.demo.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final TaxService taxService;
    private final TaxLogService taxLogService;

    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();

        List<ProductResponseDTO> productResponseDTOs = products.stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());

        return productResponseDTOs;
    }



    public ProductResponseDTO createProduct(CreateProductRequestDTO productDTO, String token) {
        Tax tax = taxService.findTaxByName(productDTO.getTaxName());
        User user = userService.getCurrentUserFromJwt(token);

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setBasePrice(productDTO.getBasePrice());
        product.setQuantity(productDTO.getQuantity());
        product.setTax(tax);
        product.setUser(user);
        product = productRepository.save(product);

        ProductResponseDTO productResponseDTO = new ProductResponseDTO(product);
        taxLogService.logTaxCalculation(productResponseDTO);
        return productResponseDTO;
    }

    public ProductResponseDTO updateProduct(String name, UpdateProductRequestDTO product, String token) {
        Tax tax = taxService.findTaxByName(product.getTaxName());
        User currentUser = userService.getCurrentUserFromJwt(token);

        Product existingProduct = productRepository.findByName(name).orElse(null);
        if (existingProduct == null) {
            return null;
        }

        if (!existingProduct.getUser().getId().equals(currentUser.getId())) {
            return null;
        }

        existingProduct.setName(product.getName());
        existingProduct.setBasePrice(product.getBasePrice());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setTax(tax);

        existingProduct = productRepository.save(existingProduct);

        return new ProductResponseDTO(existingProduct);
    }

    public boolean deleteProduct(String name, String token) {
        User currentUser = userService.getCurrentUserFromJwt(token);

        Product existingProduct = productRepository.findByName(name).orElse(null);
        if (existingProduct == null) {
            return false;
        }

        if (!existingProduct.getUser().getId().equals(currentUser.getId())) {
            return false;
        }

        productRepository.delete(existingProduct);
        return true;
    }

}