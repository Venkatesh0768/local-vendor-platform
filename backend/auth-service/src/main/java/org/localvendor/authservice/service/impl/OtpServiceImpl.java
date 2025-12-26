package org.localvendor.authservice.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.localvendor.authservice.entity.OtpRecord;
import org.localvendor.authservice.exception.OtpException;
import org.localvendor.authservice.exception.RateLimitExceededException;
import org.localvendor.authservice.repository.OtpRecordRepository;
import org.localvendor.authservice.service.EmailService;
import org.localvendor.authservice.service.OtpService;
import org.localvendor.authservice.util.OtpGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpRecordRepository otpRecordRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${otp.expiration-minutes:10}")
    private int otpExpirationMinutes;

    @Value("${otp.max-attempts:5}")
    private int maxAttempts;

    @Value("${otp.resend-cooldown-seconds:60}")
    private int resendCooldownSeconds;

    @Override
    @Transactional
    public void generateAndSendOtp(String email, OtpRecord.OtpType otpType, String ipAddress) {
        // Check rate limiting
        checkRateLimit(email, otpType);

        // Invalidate any existing active OTPs
        invalidateExistingOtps(email, otpType);

        // Generate OTP
        String otp = OtpGenerator.generate();
        String otpHash = passwordEncoder.encode(otp);

        // Calculate expiration
        Instant expiresAt = Instant.now().plus(otpExpirationMinutes, ChronoUnit.MINUTES);

        // Create OTP record
        OtpRecord otpRecord = OtpRecord.builder()
                .email(email)
                .otpHash(otpHash)
                .otpType(otpType)
                .maxAttempts(maxAttempts)
                .expiresAt(expiresAt)
                .ipAddress(ipAddress)
                .createdAt(Instant.now())
                .build();

        otpRecordRepository.save(otpRecord);

        // Send OTP via email
        sendOtpEmail(email, otp, otpType);

        log.info("OTP generated and sent to: {} for type: {}", email, otpType);
    }

    @Override
    @Transactional
    public void verifyOtp(String email, String otp, OtpRecord.OtpType otpType) {
        // Find the most recent active OTP
        OtpRecord otpRecord = otpRecordRepository
                .findTopByEmailAndOtpTypeAndUsedFalseOrderByCreatedAtDesc(email, otpType)
                .orElseThrow(() -> new OtpException("No valid OTP found. Please request a new one."));

        // Check if OTP is expired
        if (otpRecord.isExpired()) {
            throw new OtpException("OTP has expired. Please request a new one.");
        }

        // Check if max attempts exceeded
        if (!otpRecord.canAttempt()) {
            throw new OtpException("Maximum verification attempts exceeded. Please request a new OTP.");
        }

        // Verify OTP
        if (!passwordEncoder.matches(otp, otpRecord.getOtpHash())) {
            otpRecord.incrementAttempts();
            otpRecordRepository.save(otpRecord);

            int remainingAttempts = otpRecord.getMaxAttempts() - otpRecord.getAttempts();
            throw new OtpException(
                    String.format("Invalid OTP. %d attempts remaining.", remainingAttempts)
            );
        }

        // Mark OTP as used
        otpRecord.setUsed(true);
        otpRecord.setVerifiedAt(Instant.now());
        otpRecordRepository.save(otpRecord);

        log.info("OTP verified successfully for: {} type: {}", email, otpType);
    }

    @Override
    @Transactional
    public void invalidateExistingOtps(String email, OtpRecord.OtpType otpType) {
        java.util.List<OtpRecord> activeOtps = otpRecordRepository
                .findByEmailAndOtpTypeAndUsedFalse(email, otpType);

        activeOtps.forEach(otp -> otp.setUsed(true));
        otpRecordRepository.saveAll(activeOtps);

        log.debug("Invalidated {} existing OTPs for: {}", activeOtps.size(), email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOtpValid(String email, OtpRecord.OtpType otpType) {
        Optional<OtpRecord> otpRecord = otpRecordRepository
                .findTopByEmailAndOtpTypeAndUsedFalseOrderByCreatedAtDesc(email, otpType);

        return otpRecord.isPresent() &&
                !otpRecord.get().isExpired() &&
                otpRecord.get().canAttempt();
    }

    private void checkRateLimit(String email, OtpRecord.OtpType otpType) {
        Instant cooldownThreshold = Instant.now().minusSeconds(resendCooldownSeconds);

        Optional<OtpRecord> recentOtp = otpRecordRepository
                .findTopByEmailAndOtpTypeOrderByCreatedAtDesc(email, otpType);

        if (recentOtp.isPresent() && recentOtp.get().getCreatedAt().isAfter(cooldownThreshold)) {
            long secondsRemaining = resendCooldownSeconds -
                    ChronoUnit.SECONDS.between(recentOtp.get().getCreatedAt(), Instant.now());
            throw new RateLimitExceededException(
                    String.format("Please wait %d seconds before requesting a new OTP.", secondsRemaining)
            );
        }

        // Additional check: max 5 OTPs per hour
        Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
        long otpCountLastHour = otpRecordRepository
                .countByEmailAndOtpTypeAndCreatedAtAfter(email, otpType, oneHourAgo);

        if (otpCountLastHour >= 5) {
            throw new RateLimitExceededException(
                    "Too many OTP requests. Please try again after 1 hour."
            );
        }
    }

    private void sendOtpEmail(String email, String otp, OtpRecord.OtpType otpType) {
        String subject;
        String message;

        switch (otpType) {
            case EMAIL_VERIFICATION:
                subject = "Verify Your Email";
                message = String.format(
                        "Your email verification OTP is: %s\n\n" +
                                "This OTP is valid for %d minutes.\n" +
                                "Do not share this OTP with anyone.",
                        otp, otpExpirationMinutes
                );
                break;
            case PASSWORD_RESET:
                subject = "Reset Your Password";
                message = String.format(
                        "Your password reset OTP is: %s\n\n" +
                                "This OTP is valid for %d minutes.\n" +
                                "If you didn't request this, please ignore this email.",
                        otp, otpExpirationMinutes
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown OTP type: " + otpType);
        }

        emailService.sendEmail(email, subject, message);
    }
}
