package com.example.backend_spring.domain.users;

import java.math.BigDecimal;

import com.example.backend_spring.domain.users.dto.UserAdminCreationRequestDTO;
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
import com.example.backend_spring.domain.users.dto.UserClientCreationResponseDTO;
import com.example.backend_spring.domain.users.dto.UserClientCreationRequestDTO;
import com.example.backend_spring.domain.users.dto.UserLoginResponseDTO;
import com.example.backend_spring.security.encoder.PepperPasswordEncoder;
import com.example.backend_spring.security.jwt.JwtTokenProviderService;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private JwtTokenProviderService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

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

    public long countAdmins() {
        return userRepository.countByRole(UserRole.ADMIN);
    }

    public UserClientCreationResponseDTO registerUserClient(UserClientCreationRequestDTO dto) {
        if(this.userRepository.findByUsername(dto.accessUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        String encryptedAccessPassword = pepperPasswordEncoder.encode(dto.accessPassword());
        User user = new User(
            dto.accessUsername(),
            encryptedAccessPassword,
            UserRole.CLIENT
        );
        this.userRepository.save(user);

        AccountCreationDTO accountCreationDTO = new AccountCreationDTO(
            user,
            new BigDecimal(0),
            AccountStatus.ACTIVE,
            AccountType.DEFAULT,
            dto.transactionPassword()
        );
        AccountResponseDTO accountResponseDTO = accountService.create(accountCreationDTO);
        return new UserClientCreationResponseDTO(
            accountResponseDTO
        );
    }

    public void registerAdmin(UserAdminCreationRequestDTO dto) {
        if (userRepository.findByUsername(dto.accessUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        String encryptedPassword = pepperPasswordEncoder.encode(dto.accessPassword());
        User user = new User(
            dto.accessUsername(),
            encryptedPassword,
            UserRole.ADMIN
        );
        userRepository.save(user);
    }

}
