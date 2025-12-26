package org.localvendor.authservice.controller.v1;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.localvendor.authservice.dto.request.*;
import org.localvendor.authservice.dto.response.ApiResponse;
import org.localvendor.authservice.dto.response.AuthResponse;
import org.localvendor.authservice.service.AuthService;
import org.localvendor.authservice.service.PasswordService;
import org.localvendor.authservice.util.CookieUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {

    private final AuthService authService;
    private final PasswordService passwordService;
    private final CookieUtil cookieUtil;

    @PostMapping("/signup")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<Void>> signUp(@Valid @RequestBody SignUpRequest request) {
        ApiResponse<Void> response = authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/signin")
    @Operation(summary = "Sign in user")
    public ResponseEntity<ApiResponse<AuthResponse>> signIn(
            @Valid @RequestBody SignInRequest request,
            HttpServletResponse response
    ) {
        AuthResponse authResponse = authService.signIn(request);

        // Set access token in HTTP-only cookie
        cookieUtil.createCookie(
                response,
                "access_token",
                authResponse.getAccessToken(),
                authResponse.getExpiresIn()
        );

        // Set refresh token in HTTP-only cookie (longer expiration)
        // Note: Refresh token is also stored in DB
        cookieUtil.createCookie(
                response,
                "refresh_token",
                authResponse.getRefreshToken(),
                7 * 24 * 60 * 60 // 7 days
        );

        return ResponseEntity.ok(ApiResponse.success("Signed in successfully", authResponse));
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify email with OTP")
    public ResponseEntity<ApiResponse<Void>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        ApiResponse<Void> response = authService.verifyOtp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-otp")
    @Operation(summary = "Resend OTP")
    public ResponseEntity<ApiResponse<Void>> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        ApiResponse<Void> response = authService.resendOtp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = cookieUtil.getCookieValue(request, "refresh_token")
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        AuthResponse authResponse = authService.refreshToken(refreshToken);

        // Update cookies with new tokens
        cookieUtil.createCookie(
                response,
                "access_token",
                authResponse.getAccessToken(),
                authResponse.getExpiresIn()
        );

        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", authResponse));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user")
    public ResponseEntity<ApiResponse<Void>> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = cookieUtil.getCookieValue(request, "refresh_token").orElse(null);
        ApiResponse<Void> apiResponse = authService.logout(refreshToken);

        // Clear cookies
        cookieUtil.deleteCookie(response, "access_token");
        cookieUtil.deleteCookie(response, "refresh_token");

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Initiate password reset")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        ApiResponse<Void> response = passwordService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password with OTP")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        ApiResponse<Void> response = passwordService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
}

