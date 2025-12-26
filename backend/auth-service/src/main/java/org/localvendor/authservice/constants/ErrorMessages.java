package org.localvendor.authservice.constants;


public final class ErrorMessages {

    private ErrorMessages() {
        throw new IllegalStateException("Constants class");
    }

    // Validation errors
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String EMAIL_INVALID = "Invalid email format";
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PASSWORD_TOO_SHORT = "Password must be at least 8 characters";
    public static final String PASSWORD_TOO_LONG = "Password must not exceed 128 characters";
    public static final String PASSWORD_PATTERN = "Password must contain uppercase, lowercase, digit, and special character";

    // Authentication errors
    public static final String AUTH_REQUIRED = "Authentication required";
    public static final String INVALID_CREDENTIALS_MSG = "Invalid email or password";
    public static final String ACCOUNT_DISABLED = "Account is disabled";
    public static final String EMAIL_NOT_VERIFIED = "Email not verified";

    // Token errors
    public static final String TOKEN_EXPIRED = "Token has expired";
    public static final String TOKEN_INVALID = "Invalid token";
    public static final String TOKEN_REVOKED = "Token has been revoked";

    // Rate limit errors
    public static final String RATE_LIMIT_EXCEEDED = "Too many requests. Please try again later.";
    public static final String OTP_RATE_LIMIT = "Too many OTP requests. Please wait before requesting again.";
}
