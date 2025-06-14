package com.example.backend_spring.domain.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendUsernameReminder(String destinationEmail) {
        // check if e-mail exists among profiles
        // if so, fetch user linked to current JWT
        // get username of JWT
        // send email informing so

        String subject = "Your Username Reminder";
        String username = "FOO";
        String body = String.format(
                "Hello!\n\nYour username is: %s\n\nIf you didn't request this, please ignore this email.",
                username
        );

        this.sendSimpleEmail(destinationEmail, subject, body);
    }
}
