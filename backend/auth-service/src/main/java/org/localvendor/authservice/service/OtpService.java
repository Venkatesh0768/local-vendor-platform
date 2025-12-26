package org.localvendor.authservice.service;


import org.localvendor.authservice.entity.OtpRecord;

public interface OtpService {
    void generateAndSendOtp(String email, OtpRecord.OtpType otpType, String ipAddress);
    void verifyOtp(String email, String otp, OtpRecord.OtpType otpType);
    void invalidateExistingOtps(String email, OtpRecord.OtpType otpType);
    boolean isOtpValid(String email, OtpRecord.OtpType otpType);
}