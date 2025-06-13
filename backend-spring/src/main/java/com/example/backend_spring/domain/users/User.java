package com.example.backend_spring.domain.users;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails{
    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "username", nullable = false, unique = true)
    private String username; 

    @Column(name = "password", nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "TEXT")
    private UserRole role;

    public User(String username, String password, UserRole role){
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN) {
            return List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_CLIENT"),
                new SimpleGrantedAuthority("ROLE_MERCHANT")
            );
        }
        else if(this.role == UserRole.CLIENT) {
            return List.of(
                new SimpleGrantedAuthority("ROLE_CLIENT")
            );
        }

        // UserRole.MERCHANT
        return List.of(
            new SimpleGrantedAuthority("ROLE_MERCHANT")
        );
    }

    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }

    public boolean isMerchant() {
        return this.role == UserRole.MERCHANT;
    }

    public boolean isClient() {
        return this.role == UserRole.CLIENT;
    }
}