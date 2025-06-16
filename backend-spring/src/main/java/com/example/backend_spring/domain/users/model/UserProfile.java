package com.example.backend_spring.domain.users.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user_profile")
@Table(name = "user_profile")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserProfile {
    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;
    
    @Setter
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Setter
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Setter
    @Column(name = "email", nullable = false)
    private String email;

    @Setter
    @Column(name = "phone")
    private String phone;

    @Setter
    @Column(name = "address_line1")
    private String addressLine1;

    @Setter
    @Column(name = "address_line2")
    private String addressLine2;

    @Setter
    @Column(name = "city")
    private String city;

    @Setter
    @Column(name = "province")
    private String province;

    @Setter
    @Column(name = "postal_code")
    private String postalCode;

    @Setter
    @Column(name = "country")
    private String country;

    @Setter
    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    public UserProfile(
        User user,
        String firstName,
        String lastName,
        String email
    ){
        this.id = user.getId();
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}