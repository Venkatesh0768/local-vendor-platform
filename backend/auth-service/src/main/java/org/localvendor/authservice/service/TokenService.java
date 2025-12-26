package org.localvendor.authservice.service;


import org.localvendor.authservice.entity.RefreshToken;
import org.localvendor.authservice.entity.User;

public interface TokenService {
    void storeRefreshToken(User user, String token, String deviceId, String ipAddress, String userAgent);
    RefreshToken verifyRefreshToken(String token);
    void rotateRefreshToken(RefreshToken oldToken, String newToken);
    void revokeRefreshToken(String token);
    void revokeAllUserTokens(Long userId);
}