package org.localvendor.authservice.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.localvendor.authservice.exception.InvalidTokenException;
import org.localvendor.authservice.model.EmailOtp;
import org.localvendor.authservice.model.RefreshToken;
import org.localvendor.authservice.model.User;
import org.localvendor.authservice.repositories.EmailOtpRepository;
import org.localvendor.authservice.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final EmailOtpRepository   emailOtpRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailService emailService;


    @Value("${otp.expiration}")
    private long otpExpiration;

    @Value("${otp.length}")
    private int otpLength;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;


    @Transactional
    public void generateAndSendOTP(@Valid String email) {
       emailOtpRepository.deleteByEmail(email);

       // Generate OTP
        String otpCode = generateOTPCode();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(otpExpiration /1000);

        EmailOtp otp = EmailOtp.builder()
                .email(email)
                .otpCode(otpCode)
                .expiryTime(expiryTime)
                .verified(false)
                .build();

        emailOtpRepository.save(otp);

        emailService.sendOTPEmail(email , otpCode);

    }

    @Transactional
    public void generateAndSendPasswordResetOTP(@Valid String email) {
        emailOtpRepository.deleteByEmail(email);

        // Generate OTP
        String otpCode = generateOTPCode();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(otpExpiration /1000);

        EmailOtp otp = EmailOtp.builder()
                .email(email)
                .otpCode(otpCode)
                .expiryTime(expiryTime)
                .build();

        emailOtpRepository.save(otp);

        emailService.sendPasswordResetOTPEmail(email , otpCode);

        
    }

    public boolean validateOTP(String email, String otpCode) {
        Optional<EmailOtp> otpOptional = emailOtpRepository
                .findByEmailAndOtpCodeAndVerifiedFalse(email, otpCode);

        if (otpOptional.isEmpty()) {
            return false;
        }

        EmailOtp otp = otpOptional.get();

        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            return false;
        }

        otp.setVerified(true);
        emailOtpRepository.save(otp);

        return true;
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {

        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);

        RefreshToken refreshToken;
        if (existingToken.isPresent()) {
            refreshToken = existingToken.get();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000));
        } else {
            refreshToken = RefreshToken.builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000))
                    .build();
        }

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new InvalidTokenException("Refresh token expired");
        }
        return token;
    }

    private String generateOTPCode() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }


}
