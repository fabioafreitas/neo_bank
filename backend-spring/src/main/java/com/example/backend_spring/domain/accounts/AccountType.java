package com.example.backend_spring.domain.accounts;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountType {
    NORMAL("normal"),
    CASHBACK("cashback");

    @Enumerated(EnumType.STRING)
    private String type;

    public static AccountType fromString(String value) {
        for (AccountType type : AccountType.values()) {
            if (type.getType().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid type: " + value);
    }
}
