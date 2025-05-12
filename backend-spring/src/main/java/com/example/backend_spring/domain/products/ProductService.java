package com.example.backend_spring.domain.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend_spring.domain.products.dto.ProductCreationDTO;
import com.example.backend_spring.domain.products.dto.ProductResponseDTO;
import com.example.backend_spring.domain.products.dto.ProductUpdateDTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    public List<ProductResponseDTO> findAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public ProductResponseDTO findById(UUID id) {
        return repo.findById(id).map(this::toDto)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Product not found: " + id
            ));
    }

    public ProductResponseDTO create(ProductCreationDTO dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        return toDto(repo.save(product));
    }

    public ProductResponseDTO update(UUID id, ProductUpdateDTO dto) {
        Product product = repo.findById(id).orElseThrow();

        if(dto.name() != null) {
            product.setName(dto.name());
        }
        if(dto.description() != null) {
            product.setDescription(dto.description());
        }
        if(dto.price() != null) {
            product.setPrice(dto.price());
        }
        
        product.setUpdatedAt(java.time.OffsetDateTime.now());
        return toDto(repo.save(product));
    }

    public void delete(UUID id) {
        repo.deleteById(id);
    }

    private ProductResponseDTO toDto(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO(product);
        return dto;
    }
}
