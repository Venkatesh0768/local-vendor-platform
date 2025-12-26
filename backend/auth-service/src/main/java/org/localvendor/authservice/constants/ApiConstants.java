package org.localvendor.authservice.constants;

public final class ApiConstants {

    private ApiConstants() {
        throw new IllegalStateException("Constants class");
    }

    public static final String API_VERSION = "/api/v1";
    public static final String AUTH_BASE = API_VERSION + "/auth";
    public static final String USER_BASE = API_VERSION + "/users";

    // Success messages
    public static final String SIGNUP_SUCCESS = "Registration successful. Please verify your email.";
    public static final String SIGNIN_SUCCESS = "Signed in successfully.";
    public static final String LOGOUT_SUCCESS = "Logged out successfully.";
    public static final String OTP_SENT = "OTP sent successfully to your email.";
    public static final String OTP_VERIFIED = "OTP verified successfully.";
    public static final String PASSWORD_RESET_SUCCESS = "Password reset successfully.";

    // Error messages
    public static final String INVALID_CREDENTIALS = "Invalid email or password.";
    public static final String EMAIL_ALREADY_EXISTS = "Email already registered.";
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String INVALID_TOKEN = "Invalid or expired token.";
    public static final String OTP_EXPIRED = "OTP has expired. Please request a new one.";
    public static final String ACCOUNT_LOCKED = "Account is locked. Please contact support.";
}

