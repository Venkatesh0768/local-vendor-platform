package org.localvendor.authservice.service;

import org.localvendor.authservice.exception.EmailSendingException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        try {
            // Implementation for sending OTP email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Your OTP Code");
            message.setText("Your OTP code is: " + otp);
            mailSender.send(message);
        }catch (Exception e) {
           throw  new EmailSendingException("Failed to send OTP email: " + e.getMessage());
        }
    }

    public void sendPasswordResetEmail(String to, String resetLink) {
        try {
            // Implementation for sending password reset email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Password Reset Request");
            message.setText("Click the following link to reset your password: " + resetLink);
            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendingException("Failed to send password reset email: " + e.getMessage());
        }
    }

}
