package com.example.backend_spring.integration_test;

import com.example.backend_spring.domain.users.dto.UserCreationClientRequestDTO;
import com.example.backend_spring.domain.users.dto.UserCreationResponseDTO;
import com.example.backend_spring.domain.users.model.User;
import com.example.backend_spring.domain.users.repository.UserRepository;
import com.example.backend_spring.domain.users.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest extends DbConfigUnitTestBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    // TODO review this test
    @Test
    void shouldRegisterUserSuccessfullyAndPersistToDB() {
//        // GIVEN
//        String testEmail = "test@email.com";
//        String testFirstName = "Fulano";
//        String testLastName = "de Tal";
//        String testUsername = "myusr_test_" + System.currentTimeMillis(); // Use unique username
//        String testPassword = "mypsk";
//        String testTransactionPassword = "123456";
//
//        UserCreationClientRequestDTO requestDTO = new UserCreationClientRequestDTO(
//                testEmail,
//                testFirstName,
//                testLastName,
//                testUsername,
//                testPassword,
//                testTransactionPassword
//        );
//
//        // WHEN
//        UserCreationResponseDTO responseDTO = userService.registerUserClient(requestDTO);
//
//        // THEN - Part 1: Assert the returned DTO's content
//        assertNotNull(
//                responseDTO,
//                "Response DTO should not be null");
//        assertNotNull(
//                responseDTO.account(),
//                "Account Response DTO inside should not be null");
//        assertNotNull(
//                responseDTO.account().accountNumber(),
//                "Account number should not be null");
//        assertTrue(
//                responseDTO.account().accountNumber().matches("\\d{10}"),
//                "Account number should be a 10-digit number");
//        assertEquals(
//                0,
//                responseDTO.account().balance().compareTo(java.math.BigDecimal.ZERO),
//                "Account balance should be zero");
//
//        // THEN - Part 2: Verify persistence in the real database
//        Optional<User> persistedUserOptional = userRepository.findByUsername(testUsername);
//        assertTrue(
//                persistedUserOptional.isPresent(),
//                "User should be found in the database");
//
//        User persistedUser = persistedUserOptional.get();
//        assertEquals(
//                testUsername,
//                persistedUser.getUsername(),
//                "Persisted user's username should match");
    }
}
