package org.localvendor.authservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.localvendor.authservice.repository.OtpRecordRepository;
import org.localvendor.authservice.repository.RefreshTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTasks {

    private final RefreshTokenRepository refreshTokenRepository;
    private final OtpRecordRepository otpRecordRepository;

    /**
     * Clean up expired refresh tokens daily at 2 AM
     */
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("Starting cleanup of expired refresh tokens");

        int deletedCount = 0;
        var expiredTokens = refreshTokenRepository.findAll().stream()
                .filter(token -> token.isExpired() || token.isRevoked())
                .toList();

        if (!expiredTokens.isEmpty()) {
            refreshTokenRepository.deleteAll(expiredTokens);
            deletedCount = expiredTokens.size();
        }

        log.info("Cleaned up {} expired refresh tokens", deletedCount);
    }

    /**
     * Clean up expired and used OTPs every hour
     */
    @Scheduled(fixedRate = 3600000) // 1 hour
    @Transactional
    public void cleanupExpiredOtps() {
        log.info("Starting cleanup of expired OTPs");
        otpRecordRepository.cleanupExpiredAndUsedOtps(Instant.now());
        log.info("Expired OTPs cleaned up");
    }
}