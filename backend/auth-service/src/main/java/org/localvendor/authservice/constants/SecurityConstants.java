package org.localvendor.authservice.constants;

public final class SecurityConstants {

    private SecurityConstants() {
        throw new IllegalStateException("Constants class");
    }

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String ACCESS_TOKEN_COOKIE = "access_token";
    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    public static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;
    public static final int ACCOUNT_LOCK_DURATION_MINUTES = 30;

    public static final int OTP_LENGTH = 6;
    public static final int OTP_EXPIRATION_MINUTES = 10;
    public static final int OTP_MAX_ATTEMPTS = 5;

    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 128;
}