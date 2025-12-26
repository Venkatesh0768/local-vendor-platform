package org.localvendor.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.localvendor.authservice.entity.RefreshToken;
import org.localvendor.authservice.entity.User;
import org.localvendor.authservice.exception.TokenException;
import org.localvendor.authservice.repository.RefreshTokenRepository;
import org.localvendor.authservice.security.JwtTokenProvider;
import org.localvendor.authservice.service.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void storeRefreshToken(
            User user,
            String token,
            String deviceId,
            String ipAddress,
            String userAgent
    ) {
        // Hash the token before storing
        String tokenHash = passwordEncoder.encode(token);

        Instant expiresAt = Instant.now().plusMillis(tokenProvider.getRefreshTokenExpiration());

        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(tokenHash)
                .user(user)
                .deviceId(deviceId)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .expiresAt(expiresAt)
                .createdAt(Instant.now())
                .build();

        refreshTokenRepository.save(refreshToken);

        // Clean up expired tokens for this user
        cleanupExpiredTokens(user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshToken verifyRefreshToken(String token) {
        // Find all non-revoked tokens for potential matching
        java.util.List<RefreshToken> activeTokens = refreshTokenRepository
                .findByRevokedFalseAndExpiresAtAfter(Instant.now());

        // Find matching token by comparing hashes
        RefreshToken refreshToken = activeTokens.stream()
                .filter(t -> passwordEncoder.matches(token, t.getTokenHash()))
                .findFirst()
                .orElseThrow(() -> new TokenException("Invalid or expired refresh token"));

        if (!refreshToken.isValid()) {
            throw new TokenException("Refresh token is invalid or expired");
        }

        return refreshToken;
    }

    @Override
    @Transactional
    public void rotateRefreshToken(RefreshToken oldToken, String newToken) {
        // Revoke old token
        oldToken.setRevoked(true);
        oldToken.setRevokedAt(Instant.now());
        oldToken.setRotationCount(oldToken.getRotationCount() + 1);
        refreshTokenRepository.save(oldToken);

        // Store new token
        String tokenHash = passwordEncoder.encode(newToken);
        Instant expiresAt = Instant.now().plusMillis(tokenProvider.getRefreshTokenExpiration());

        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(tokenHash)
                .user(oldToken.getUser())
                .deviceId(oldToken.getDeviceId())
                .ipAddress(oldToken.getIpAddress())
                .userAgent(oldToken.getUserAgent())
                .expiresAt(expiresAt)
                .rotationCount(oldToken.getRotationCount())
                .createdAt(Instant.now())
                .build();

        refreshTokenRepository.save(refreshToken);

        log.info("Refresh token rotated for user: {}", oldToken.getUser().getEmail());
    }

    @Override
    @Transactional
    public void revokeRefreshToken(String token) {
        try {
            RefreshToken refreshToken = verifyRefreshToken(token);
            refreshToken.setRevoked(true);
            refreshToken.setRevokedAt(Instant.now());
            refreshTokenRepository.save(refreshToken);
            log.info("Refresh token revoked");
        } catch (Exception e) {
            log.warn("Failed to revoke token: {}", e.getMessage());
        }
    }

    @Override
    @Transactional
    public void revokeAllUserTokens(Long userId) {
        java.util.List<RefreshToken> userTokens = refreshTokenRepository
                .findByUserIdAndRevokedFalse(userId);

        userTokens.forEach(token -> {
            token.setRevoked(true);
            token.setRevokedAt(Instant.now());
        });

        refreshTokenRepository.saveAll(userTokens);
        log.info("All tokens revoked for user ID: {}", userId);
    }

    private void cleanupExpiredTokens(Long userId) {
        refreshTokenRepository.deleteExpiredTokens(Instant.now(), userId);
    }
}