package com.example.backend_spring.domain.users.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend_spring.domain.users.dto.UserGeneralMessageResponseDTO;
import com.example.backend_spring.domain.users.model.UserProfile;
import com.example.backend_spring.domain.users.repository.UserProfileRepository;

@Service
public class EmailService {

    @Autowired
    private EmailAsyncService emailAsyncService;

    @Autowired
    private UserProfileRepository userProfileRepository;


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

    public void sendAccessPasswordResetUrl(String destinationEmail, String token) {
        String subject = "Simple Bank: Access Password Reset";
        String body = """
            Greetings dear client!

            Access this url to recover you access password: https://myurl.com/auth/recoverAccessPassword/%s

            This link will expire in 30 minutes.
            If you did not request this, please ignore this email.
            """.formatted(token);
        emailAsyncService.sendSimpleEmail(destinationEmail, subject, body);
    } 
}
