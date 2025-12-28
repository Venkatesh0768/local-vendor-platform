package org.localvendor.authservice.service;

public interface EmailService {
    void sendOTPEmail(String to, String otpCode);
    void sendPasswordResetOTPEmail(String to, String otpCode);
}
