package com.example.backend_spring.domain.merchants.service;

import java.util.List;
import java.util.UUID;

import com.example.backend_spring.domain.merchants.dto.MerchantProfileResponseDTO;
import com.example.backend_spring.domain.merchants.dto.MerchantRequestDTO;
import com.example.backend_spring.domain.merchants.dto.MerchantResponseDTO;
import com.example.backend_spring.domain.merchants.model.Merchant;
import com.example.backend_spring.domain.merchants.repository.MerchantRepository;
import com.example.backend_spring.domain.users.model.User;
import com.example.backend_spring.domain.users.model.UserProfile;
import com.example.backend_spring.domain.users.service.UserProfileService;
import com.example.backend_spring.domain.users.utils.UserRole;
import com.example.backend_spring.security.jwt.JwtTokenProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final JwtTokenProviderService jwtTokenProviderService;
    private final UserProfileService userProfileService;

    public MerchantService(
            MerchantRepository merchantRepository,
            JwtTokenProviderService jwtTokenProviderService,
            UserProfileService userProfileService
    ) {
        this.merchantRepository = merchantRepository;
        this.jwtTokenProviderService = jwtTokenProviderService;
        this.userProfileService = userProfileService;
    }

    public List<MerchantProfileResponseDTO> getAllMerchants() {
        List<Merchant> merchants = merchantRepository.findAll();
        return merchants.stream()
                .map(this::toMerchantProfileDto)
                .toList();
    }

    public MerchantProfileResponseDTO getMerchantById(UUID merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Merchant not found"));
        return this.toMerchantProfileDto(merchant);
    }

    private Merchant findCurrentMerchant() {
        User user = jwtTokenProviderService.getContextUser();
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (user.getRole() != UserRole.MERCHANT) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a merchant");
        }

        return merchantRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Merchant not found"));
    }

    public MerchantProfileResponseDTO getCurrentMerchantProfile() {
        return this.toMerchantProfileDto(this.findCurrentMerchant());
    }

    public MerchantProfileResponseDTO updateCurrentMerchantProfile(MerchantRequestDTO dto) {
        Merchant merchant = this.findCurrentMerchant();

        if (dto.storeName() != null) merchant.setStoreName(dto.storeName());
        if (dto.description() != null) merchant.setDescription(dto.description());

        merchantRepository.save(merchant);
        return this.toMerchantProfileDto(merchant);
    }

    public Merchant create(User user, String storeName, String description) {
        Merchant merchant = new Merchant(user, storeName, description);
        return merchantRepository.save(merchant);
    }

    public MerchantResponseDTO toDto(Merchant merchant) {
        return new MerchantResponseDTO(
                merchant.getId(),
                merchant.getStoreName(),
                merchant.getDescription()
        );
    }

    public MerchantProfileResponseDTO toMerchantProfileDto(Merchant merchant) {
        return new MerchantProfileResponseDTO(
                this.toDto(merchant),
                userProfileService.toDto(
                        merchant.getUser().getUserProfile()
                )
        );
    }
}