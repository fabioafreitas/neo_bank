package com.example.backend_spring.domain.users;

public enum UserRole {
    ADMIN,
    USER;
}

// import com.fasterxml.jackson.annotation.JsonCreator;
// import jakarta.persistence.EnumType;
// import jakarta.persistence.Enumerated;
// import lombok.AllArgsConstructor;
// import lombok.Getter;

// @Getter
// @AllArgsConstructor
// public enum UserRole {
//     ADMIN("admin"),
//     USER("user");

//     @Enumerated(EnumType.STRING)
//     private String role;

//     @JsonCreator
//     public static UserRole fromString(String value) {
//         for (UserRole role : UserRole.values()) {
//             if (role.getRole().equalsIgnoreCase(value)) {
//                 return role;
//             }
//         }
//         throw new IllegalArgumentException("Invalid role: " + value);
//     }
// }
