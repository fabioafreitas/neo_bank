package com.example.backend_spring.domain.users.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.backend_spring.domain.users.dto.UserGeneralMessageResponseDTO;
import com.example.backend_spring.domain.users.model.UserProfile;
import com.example.backend_spring.domain.users.repository.UserProfileRepository;
import com.example.backend_spring.domain.users.utils.PasswordResetRequestType;

@Service
public class EmailService {

    @Autowired
    private EmailAsyncService emailAsyncService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Value("${frontend.url}")
    private String frontendUrl;
    
    @Value("${frontend.reset-password-endpoint}")
    private String frontendResetPasswordEndpoint;

    public UserGeneralMessageResponseDTO sendUsernameReminder(String destinationEmail) {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByEmail(destinationEmail);

        if (userProfileOpt.isPresent()) {
            String subject = "Simple Bank: Username Reminder";
            String username = userProfileOpt.get().getUser().getUsername();
            String body = String.format(
                    "Greetings dear client!\n\nYour username is: %s\n\nIf you didn't request this, please ignore this email.",
                    username
            );
            emailAsyncService.sendSimpleEmail(destinationEmail, subject, body);
        }

        return new UserGeneralMessageResponseDTO("Username reminder sent to " + destinationEmail);
    }

    public void sendPasswordResetUrl(String destinationEmail, String token, PasswordResetRequestType type) {
        String passwordType = type == PasswordResetRequestType.LOGIN_PASSWORD ? "Access" : "Transcation";
        String subject = "Simple Bank: %s Password Reset".formatted(passwordType);
        String urlRecoverPassword = "%s%s/%s".formatted(frontendUrl, frontendResetPasswordEndpoint, token);
        String body = """
            Greetings dear client!

            Use this link to recover you %s password: %s

            The link will expire in 30 minutes.
            If you did not request this, please ignore this email.
            """.formatted(passwordType.toLowerCase(), urlRecoverPassword);
        emailAsyncService.sendSimpleEmail(destinationEmail, subject, body);
    } 
}
