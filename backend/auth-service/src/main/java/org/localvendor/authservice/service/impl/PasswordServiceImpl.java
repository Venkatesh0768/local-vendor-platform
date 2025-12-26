package org.localvendor.authservice.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.localvendor.authservice.dto.request.ForgotPasswordRequest;
import org.localvendor.authservice.dto.request.ResetPasswordRequest;
import org.localvendor.authservice.dto.response.ApiResponse;
import org.localvendor.authservice.entity.OtpRecord;
import org.localvendor.authservice.entity.User;
import org.localvendor.authservice.exception.UserNotFoundException;
import org.localvendor.authservice.repository.UserRepository;
import org.localvendor.authservice.service.OtpService;
import org.localvendor.authservice.service.PasswordService;
import org.localvendor.authservice.util.IpAddressUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest request;

    @Override
    @Transactional
    public ApiResponse<Void> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        // Verify user exists
        User user = userRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with this email"));

        // Generate and send OTP
        String ipAddress = IpAddressUtil.getClientIpAddress(request);
        otpService.generateAndSendOtp(
                forgotPasswordRequest.getEmail(),
                OtpRecord.OtpType.PASSWORD_RESET,
                ipAddress
        );

        log.info("Password reset OTP sent to: {}", forgotPasswordRequest.getEmail());

        return ApiResponse.success(
                "Password reset OTP has been sent to your email."
        );
    }

    @Override
    @Transactional
    public ApiResponse<Void> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        // Verify OTP
        otpService.verifyOtp(
                resetPasswordRequest.getEmail(),
                resetPasswordRequest.getOtp(),
                OtpRecord.OtpType.PASSWORD_RESET
        );

        // Get user
        User user = userRepository.findByEmail(resetPasswordRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Update password
        user.setPasswordHash(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));

        // Reset failed login attempts and unlock account
        user.resetFailedLoginAttempts();

        userRepository.save(user);

        log.info("Password reset successfully for: {}", resetPasswordRequest.getEmail());

        return ApiResponse.success("Password reset successfully. You can now sign in with your new password.");
    }
}