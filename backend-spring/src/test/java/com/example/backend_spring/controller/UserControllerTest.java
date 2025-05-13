package com.example.backend_spring.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.backend_spring.domain.users.UserService;
import com.example.backend_spring.domain.users.dto.UserRequestDTO;

import io.restassured.RestAssured;
import static org.hamcrest.Matchers.*;


@TestInstance(Lifecycle.PER_CLASS)
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    // Make sure to start the container before the tests
    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @BeforeAll
    private void init() {
        userService.registerUser(new UserRequestDTO("bob", "pass123"));
    }

    @BeforeEach
    private void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    public void shouldRegisterUser() {
        UserRequestDTO dto = new UserRequestDTO("ana", "pass123");

        RestAssured.given()
            .contentType("application/json")
            .body(dto)
        .when()
            .post("/api/auth/register")
        .then()
            .statusCode(201);
    }

    @Test
    public void shouldLoginUser() {
         UserRequestDTO dto = new UserRequestDTO("bob", "pass123");

        RestAssured.given()
            .contentType("application/json")
            .body(dto)
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(200)
            .body("token", notNullValue())                     // field exists
            .body("token", matchesPattern("^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$")); // basic JWT format
    }
}
