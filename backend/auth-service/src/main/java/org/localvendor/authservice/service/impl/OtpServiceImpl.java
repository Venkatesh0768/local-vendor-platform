package org.localvendor.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.localvendor.authservice.model.OTP;
import org.localvendor.authservice.repositories.OTPRepository;
import org.localvendor.authservice.service.OTPService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OTPService {

    @Value("${otp.length}")
    private int otpLength;

    @Value("${otp.expiration}")
    private long otpExpiration;


    private final EmailServiceImpl emailService;
    private final OTPRepository otpRepository;

    @Override
    public String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    @Override
    public void generateAndSendOtp(String email) {
        //Delete Otps of the Email
        otpRepository.deleteOTPByEmail(email);

        //Generate New Otp
        String otpCode = generateOtp();
        LocalDateTime expiry = LocalDateTime.now().plusSeconds(otpExpiration / 1000);

        OTP otp = OTP.builder()
                .email(email)
                .otpCode(otpCode)
                .expiryTime(expiry)
                .build();

        otpRepository.save(otp);

        emailService.sendOTPEmail(email, otpCode);
    }

    @Override
    public void generateAndSendPasswordResetOTP(String email) {

    }

    @Override
    public boolean validateOtp(String email, String otp) {
        return false;
    }
}
