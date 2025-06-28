package com.example.backend_spring.domain.users.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

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
import com.example.backend_spring.domain.users.dto.UserCreationResponseDTO;
import com.example.backend_spring.domain.users.dto.UserGeneralMessageResponseDTO;
import com.example.backend_spring.domain.users.dto.UserCreationRequestDTO;
import com.example.backend_spring.domain.users.dto.UserLoginResponseDTO;
import com.example.backend_spring.domain.users.dto.UserResetAccessPasswordRequestDTO;
import com.example.backend_spring.domain.users.dto.UserResetTransactionPasswordRequestDTO;
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
    private JwtTokenProviderService tokenProviderService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PepperPasswordEncoder pepperPasswordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).get();
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
    
    public UserCreationResponseDTO registerUserClient(UserCreationRequestDTO dto) {
        return registerUser(dto, UserRole.CLIENT);
    }

    public void registerUserMerchant(UserCreationRequestDTO dto) {
       registerUser(dto, UserRole.MERCHANT);
    }

    public void registerUserAdmin(UserCreationRequestDTO dto) {
       registerUser(dto, UserRole.ADMIN);
    }

    private UserCreationResponseDTO registerUser(UserCreationRequestDTO dto, UserRole role) {
        if(!accountService.isValidAccessPassword(dto.accessPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid access password, must have at leas 8 digits, one uppercase letter, one lowercase letter, one special character and one number");
        }
        if( UserRole.CLIENT == role &&
            !accountService.isValidTransactionPassword(dto.transactionPassword()) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction password, must be 6 numeric digits");
        }
        if(this.userRepository.findByUsername(dto.accessUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        if(this.userProfileRepository.findByEmail(dto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        
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
        this.userRepository.save(user);
        
        // If the user is not a client, we do not create aa bank account
        if(UserRole.CLIENT != role) {
            return new UserCreationResponseDTO(null);
        }
        
        AccountCreationDTO accountCreationDTO = new AccountCreationDTO(
            user,
            new BigDecimal(0),
            AccountStatus.ACTIVE,
            dto.transactionPassword()
        );
        AccountResponseDTO accountResponseDTO = accountService.create(accountCreationDTO);
        return new UserCreationResponseDTO(
            accountResponseDTO
        );
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
        if(!accountService.isValidAccessPassword(dto.newAccessPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid access password, must have at leas 8 digits, one uppercase letter, one lowercase letter, one special character and one number");
        }
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
        if(!accountService.isValidTransactionPassword(dto.newTransactionPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction password, must have at leas 8 digits, one uppercase letter, one lowercase letter, one special character and one number");
        }
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
        if(!accountService.isValidTransactionPassword(dto.newTransactionPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction password, must be 6 numeric digits");
        }

        Account account = accountService.findAccountByUser(user);
        String encryptedTransactionPassword = pepperPasswordEncoder.encode(dto.newTransactionPassword());
        account.setTransactionPassword(encryptedTransactionPassword);
        accountService.update(account);
        passwordResetRequestRepository.delete(resetRequest);
        return new UserGeneralMessageResponseDTO("Transaction password successfully reset");
    }
}
