package com.example.backend_spring.domain.users;

import java.math.BigDecimal;

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
import com.example.backend_spring.domain.users.dto.UserCreationDTO;
import com.example.backend_spring.domain.users.dto.UserRequestDTO;
import com.example.backend_spring.domain.users.dto.UserResponseDTO;
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

    public UserResponseDTO authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> pepperPasswordEncoder.matches(password, user.getPassword()))
                .map(user -> {
                    String token = tokenService.generateToken(user.getUsername(), user.getRole().name());
                    return new UserResponseDTO(token);
                })
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid credentials"
                ));
                
    }

    public long countAdmins() {
        return userRepository.countByRole(UserRole.ADMIN);
    }

    public UserCreationDTO registerUser(UserRequestDTO dto) {
        if(this.userRepository.findByUsername(dto.username()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        String encryptedPassword = pepperPasswordEncoder.encode(dto.password());
        User user = new User(
            dto.username(),
            encryptedPassword,
            UserRole.USER
        );
        this.userRepository.save(user);

        // Create an account for the new user
        AccountCreationDTO accountCreationDTO = new AccountCreationDTO(
            user, 
            new BigDecimal(0),
            AccountStatus.ACTIVE,
            AccountType.NORMAL
        );
        AccountResponseDTO accountResponseDTO = accountService.create(accountCreationDTO);
        return new UserCreationDTO(
            accountResponseDTO
        );
    }

    public void registerAdmin(UserRequestDTO dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        String encryptedPassword = pepperPasswordEncoder.encode(dto.password());
        User user = new User(
            dto.username(),
            encryptedPassword,
            UserRole.ADMIN
        );
        userRepository.save(user);
    }

}
