package com.example.backend_spring.domain.accounts;

import org.springframework.web.bind.annotation.*;

import com.example.backend_spring.domain.products.dto.ProductCreationDTO;
import com.example.backend_spring.domain.products.dto.ProductResponseDTO;
import com.example.backend_spring.domain.products.dto.ProductUpdateDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductResponseDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getById(@PathVariable("id") UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public ProductResponseDTO create(@RequestBody ProductCreationDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO update(@PathVariable("id") UUID id, @RequestBody ProductUpdateDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }
}
