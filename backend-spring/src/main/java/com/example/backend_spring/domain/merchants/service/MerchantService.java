package com.example.backend_spring.domain.merchants.service;

import java.util.List;
import java.util.UUID;

import com.example.backend_spring.domain.merchants.dto.MerchantResponseDTO;
import com.example.backend_spring.domain.merchants.model.Merchant;
import com.example.backend_spring.domain.merchants.repository.MerchantRepository;
import com.example.backend_spring.domain.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public Merchant create(User user, String storeName, String description) {
        Merchant merchant = new Merchant(user, storeName, description);
        return merchantRepository.save(merchant);
    }

    public MerchantResponseDTO toDto(Merchant merchant) {
        return new MerchantResponseDTO(
                merchant.getStoreName(),
                merchant.getDescription()
        );
    }
}