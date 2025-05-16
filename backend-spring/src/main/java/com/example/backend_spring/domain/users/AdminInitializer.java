// package com.example.backend_spring.domain.users;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;

// import com.example.backend_spring.domain.users.dto.UserRequestDTO;

// @Component
// public class AdminInitializer implements CommandLineRunner {

//     @Autowired
//     private UserService userService;

//     @Value("${default.admin.username}")
//     private String adminUsername;
    
//     @Value("${default.admin.password}")
//     private String adminPassword;

//     @Override
//     public void run(String... args) {
//         if (userService.countAdmins() == 0) {
//             userService.registerAdmin(new UserRequestDTO(adminUsername, adminPassword));
//         }
//     }
// }

