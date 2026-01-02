package org.localvendor.backend.auth.dto;

public record RefreshTokenRequest(
        String refreshToken
) {
}