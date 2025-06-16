package com.example.backend_spring.domain.users.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.backend_spring.domain.users.model.UserProfile;
import com.example.backend_spring.domain.users.repository.UserProfileRepository;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Async
    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendUsernameReminder(String destinationEmail) {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByEmail(destinationEmail);

        if (userProfileOpt.isPresent()) {
            String subject = "Simple Bank: Username Reminder";
            String username = userProfileOpt.get().getUser().getUsername();
            String body = String.format(
                    "Greetings dear client!\n\nYour username is: %s\n\nIf you didn't request this, please ignore this email.",
                    username
            );
            this.sendSimpleEmail(destinationEmail, subject, body);
        }
    }
}
