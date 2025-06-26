package com.example.backend_spring.domain.merchants.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class MerchantProductService {

    public List<Object> findByCurrentMerchant() {
        // TODO: implement method
        return null;
    }

    public Object createProduct(Object dto) {
        // TODO: implement method
        return null;
    }

    public Object findByIdAndCurrentMerchant(UUID productId) {
        // TODO: implement method
        return null;
    }

    public Object updateProduct(UUID productId, Object dto) {
        // TODO: implement method
        return null;
    }

    public void deleteProduct(UUID productId) {
        // TODO: implement method
    }

    public List<Object> findByMerchantId(UUID merchantId) {
        // TODO: implement method
        return null;
    }
}