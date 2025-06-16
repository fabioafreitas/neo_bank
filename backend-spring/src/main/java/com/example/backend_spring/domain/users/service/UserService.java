package com.example.backend_spring.domain.users.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend_spring.domain.accounts.AccountService;
import com.example.backend_spring.domain.accounts.AccountStatus;
import com.example.backend_spring.domain.accounts.AccountType;
import com.example.backend_spring.domain.accounts.dto.AccountCreationDTO;
import com.example.backend_spring.domain.accounts.dto.AccountResponseDTO;
import com.example.backend_spring.domain.users.dto.UserCreationResponseDTO;
import com.example.backend_spring.domain.users.dto.UserGeneralMessageResponseDTO;
import com.example.backend_spring.domain.users.dto.UserCreationRequestDTO;
import com.example.backend_spring.domain.users.dto.UserLoginResponseDTO;
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
        if(
            UserRole.CLIENT == role &&
            !accountService.isValidTransactionPassword(dto.transactionPassword())
        ) {
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
            AccountType.DEFAULT,
            dto.transactionPassword()
        );
        AccountResponseDTO accountResponseDTO = accountService.create(accountCreationDTO);
        return new UserCreationResponseDTO(
            accountResponseDTO
        );
    }


    public UserGeneralMessageResponseDTO requestAccessPasswordReset(String destinationEmail, PasswordResetRequestType type) {
        Optional<UserProfile> userProfile = userProfileRepository.findByEmail(destinationEmail);
        if (userProfile.isPresent()) {
            PasswordResetRequest request = new PasswordResetRequest(
                userProfile.get().getUser(),
                type
            );
            passwordResetRequestRepository.save(request);
            UUID token = request.getToken();
            emailService.sendAccessPasswordResetUrl(destinationEmail, token.toString());
        }
        return new UserGeneralMessageResponseDTO("If email ("+destinationEmail+") is correct, a recover link will arrive shrotly");
    }
}
