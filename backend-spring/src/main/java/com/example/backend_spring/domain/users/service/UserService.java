package com.example.backend_spring.domain.users.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import com.example.backend_spring.domain.merchants.dto.MerchantResponseDTO;
import com.example.backend_spring.domain.merchants.model.Merchant;
import com.example.backend_spring.domain.merchants.service.MerchantService;
import com.example.backend_spring.domain.users.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend_spring.domain.accounts.dto.AccountCreationDTO;
import com.example.backend_spring.domain.accounts.dto.AccountResponseDTO;
import com.example.backend_spring.domain.accounts.model.Account;
import com.example.backend_spring.domain.accounts.service.AccountService;
import com.example.backend_spring.domain.accounts.utils.AccountStatus;
import com.example.backend_spring.domain.users.model.PasswordResetRequest;
import com.example.backend_spring.domain.users.model.User;
import com.example.backend_spring.domain.users.model.UserProfile;
import com.example.backend_spring.domain.users.utils.PasswordResetRequestType;
import com.example.backend_spring.domain.users.utils.UserRole;
import com.example.backend_spring.domain.users.repository.PasswordResetRequestRepository;
import com.example.backend_spring.domain.users.repository.UserProfileRepository;
import com.example.backend_spring.domain.users.repository.UserRepository;
import com.example.backend_spring.security.encoder.PepperPasswordEncoder;
import com.example.backend_spring.security.jwt.JwtTokenProviderService;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private JwtTokenProviderService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private PasswordResetRequestRepository passwordResetRequestRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private JwtTokenProviderService tokenProviderService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PepperPasswordEncoder pepperPasswordEncoder;
	@Autowired
	private MerchantService merchantService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public UserLoginResponseDTO authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> pepperPasswordEncoder.matches(password, user.getPassword()))
                .map(user -> {
                    String token = tokenService.generateToken(user.getUsername(), user.getRole().name());
                    return new UserLoginResponseDTO(token);
                })
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid credentials"
                ));
                
    }

    private void validateDuplicatedUsernameAndEmail(String username, String email) {
        if(this.userRepository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        if(this.userProfileRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
    }

    private User registerUser(UserCreationBasicInfoRequestDTO dto, UserRole role) {
        this.validateDuplicatedUsernameAndEmail(
                dto.accessUsername(), dto.email()
        );
        String encryptedAccessPassword = pepperPasswordEncoder.encode(dto.accessPassword());
        User user = new User(
                dto.accessUsername(),
                encryptedAccessPassword,
                role
        );
        UserProfile userProfile = new UserProfile(
                user,
                dto.firstName(),
                dto.lastName(),
                dto.email()
        );
        // Setting userProfile in user, to tell JPA that it is a bidirectional relationship
        // and that it should persist the userProfile when saving the user
        user.setUserProfile(userProfile);
        return userRepository.save(user);
    }

    public UserCreationResponseDTO registerUserClient(UserCreationClientRequestDTO dto) {
        User user = registerUser(dto.userInfo(), UserRole.CLIENT);
        Account account = accountService.create(user, dto.accountInfo().transactionPassword());
        return this.toDto(user.getUserProfile(), account,null);
    }

    public UserCreationResponseDTO registerUserMerchant(UserCreationMerchantRequestDTO dto) {
        User user = registerUser(dto.userInfo(), UserRole.MERCHANT);
        Merchant merchant = merchantService.create(
                user,
                dto.merchantInfo().storeName(),
                dto.merchantInfo().description()
        );
        return this.toDto(user.getUserProfile(), null, merchant);
    }

    public UserCreationResponseDTO registerUserAdmin(UserCreationAdminRequestDTO dto) {
        User contextUser = tokenProviderService.getContextUser();
        if(contextUser != null && !contextUser.isAdmin()) {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized to register admin users");
        }
        User user = registerUser(dto.userInfo(), UserRole.ADMIN);
        return this.toDto(user.getUserProfile(), null,null);
    }

    public UserGeneralMessageResponseDTO requestPasswordReset(String destinationEmail, PasswordResetRequestType type) {
        if(PasswordResetRequestType.TRANSACTION_PASSWORD == type) {
            User contextUser = tokenProviderService.getContextUser();
            String contextEmail = contextUser.getUserProfile().getEmail();
            if(!contextEmail.equals(destinationEmail)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Transaction password reset is only allowed for the authenticated user");
            }
        }
        Optional<UserProfile> userProfile = userProfileRepository.findByEmail(destinationEmail);
        if (userProfile.isPresent()) { 
            User user = userProfile.get().getUser();
            passwordResetRequestRepository.deleteByUserIdAndType(user.getId(), type);
            PasswordResetRequest request = new PasswordResetRequest(user, type);
            passwordResetRequestRepository.save(request);
            UUID token = request.getToken();
            emailService.sendPasswordResetUrl(destinationEmail, token.toString(), type);
        }
        return new UserGeneralMessageResponseDTO("If email ("+destinationEmail+") is correct, a recover link will arrive shrotly");
    }

    public UserGeneralMessageResponseDTO resetAccessPassword(UserResetAccessPasswordRequestDTO dto) {
        PasswordResetRequest resetRequest = passwordResetRequestRepository.findByToken(dto.resetRequestToken())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Password reset request not found"));

        if(!resetRequest.getType().equals(PasswordResetRequestType.LOGIN_PASSWORD)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password reset request type");
        }

        // Check if the reset request has expired
        if(resetRequest.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password reset request has expired");
        }
        
        User user = resetRequest.getUser();
        String encryptedAccessPassword = pepperPasswordEncoder.encode(dto.newAccessPassword());
        user.setPassword(encryptedAccessPassword);
        userRepository.save(user);
        passwordResetRequestRepository.delete(resetRequest);
        return new UserGeneralMessageResponseDTO("Access password successfully reset");
    }

    public UserGeneralMessageResponseDTO resetTransactionPassword(UserResetTransactionPasswordRequestDTO dto) {
        PasswordResetRequest resetRequest = passwordResetRequestRepository.findByToken(dto.resetRequestToken())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Password reset request not found"));
        
        if(!resetRequest.getType().equals(PasswordResetRequestType.TRANSACTION_PASSWORD)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password reset request type");
        }
        
        // Check if the reset request has expired
        if(resetRequest.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password reset request has expired");
        }

        User user = resetRequest.getUser();
        String dbEncryptedAccessPassword = user.getPassword();
        if(!pepperPasswordEncoder.matches(dto.accessPassword(), dbEncryptedAccessPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access password does not match the one in the database");
        }

        Account account = accountService.findAccountByUser(user);
        String encryptedTransactionPassword = pepperPasswordEncoder.encode(dto.newTransactionPassword());
        account.setTransactionPassword(encryptedTransactionPassword);
        accountService.update(account);
        passwordResetRequestRepository.delete(resetRequest);
        return new UserGeneralMessageResponseDTO("Transaction password successfully reset");
    }

    public UserCreationResponseDTO toDto(
            UserProfile userProfile,
            Account account,
            Merchant merchant
    ) {
        return new UserCreationResponseDTO(
                this.userProfileService.toDto(userProfile),
                Optional.ofNullable(account)
                        .map(accountService::toDto)
                        .orElse(null),
                Optional.ofNullable(merchant)
                        .map(merchantService::toDto)
                        .orElse(null)
        );
    }
}
