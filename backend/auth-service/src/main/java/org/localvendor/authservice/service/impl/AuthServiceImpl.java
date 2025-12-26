package org.localvendor.authservice.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.localvendor.authservice.dto.request.ResendOtpRequest;
import org.localvendor.authservice.dto.request.SignInRequest;
import org.localvendor.authservice.dto.request.SignUpRequest;
import org.localvendor.authservice.dto.request.VerifyOtpRequest;
import org.localvendor.authservice.dto.response.ApiResponse;
import org.localvendor.authservice.dto.response.AuthResponse;
import org.localvendor.authservice.dto.response.UserResponse;
import org.localvendor.authservice.entity.OtpRecord;
import org.localvendor.authservice.entity.RefreshToken;
import org.localvendor.authservice.entity.Role;
import org.localvendor.authservice.entity.User;
import org.localvendor.authservice.exception.*;
import org.localvendor.authservice.repository.RefreshTokenRepository;
import org.localvendor.authservice.repository.RoleRepository;
import org.localvendor.authservice.repository.UserRepository;
import org.localvendor.authservice.security.JwtTokenProvider;
import org.localvendor.authservice.service.AuthService;
import org.localvendor.authservice.service.OtpService;
import org.localvendor.authservice.service.TokenService;
import org.localvendor.authservice.util.IpAddressUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final OtpService otpService;
    private final TokenService tokenService;
    private final HttpServletRequest request;

    @Override
    @Transactional
    public ApiResponse<Void> signUp(SignUpRequest signUpRequest) {
        // Check if user already exists
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        // Create user entity
        User user = User.builder()
                .email(signUpRequest.getEmail())
                .passwordHash(passwordEncoder.encode(signUpRequest.getPassword()))
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .emailVerified(false)
                .active(false) // Account inactive until email verified
                .build();

        // Assign default USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRoles(Set.of(userRole));

        userRepository.save(user);

        // Send OTP for email verification
        String ipAddress = IpAddressUtil.getClientIpAddress(request);
        otpService.generateAndSendOtp(
                signUpRequest.getEmail(),
                OtpRecord.OtpType.EMAIL_VERIFICATION,
                ipAddress
        );

        log.info("User registered successfully: {}", signUpRequest.getEmail());

        return ApiResponse.success(
                "Registration successful. Please verify your email with the OTP sent."
        );
    }

    @Override
    @Transactional
    public AuthResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        // Check if account is locked
        if (user.isAccountLocked()) {
            throw new AuthException("Account is locked due to multiple failed login attempts");
        }

        // Check if email is verified
        if (!user.isEmailVerified()) {
            throw new AuthException("Email not verified. Please verify your email first.");
        }

        // Check if account is active
        if (!user.isActive()) {
            throw new AuthException("Account is deactivated. Please contact support.");
        }

        try {
            // Authenticate
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.getEmail(),
                            signInRequest.getPassword()
                    )
            );

            // Reset failed login attempts on successful login
            user.resetFailedLoginAttempts();
            user.setLastLoginAt(Instant.now());
            userRepository.save(user);

            // Generate tokens
            String accessToken = tokenProvider.generateAccessToken(authentication);
            String refreshToken = tokenProvider.generateRefreshToken(user.getEmail());

            // Store refresh token
            String ipAddress = IpAddressUtil.getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");
            tokenService.storeRefreshToken(
                    user,
                    refreshToken,
                    signInRequest.getDeviceId(),
                    ipAddress,
                    userAgent
            );

            // Build response
            Set<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            UserResponse userResponse = UserResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .emailVerified(user.isEmailVerified())
                    .roles(roles)
                    .lastLoginAt(user.getLastLoginAt())
                    .createdAt(user.getCreatedAt())
                    .build();

            log.info("User signed in successfully: {}", user.getEmail());

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(tokenProvider.getAccessTokenExpiration() / 1000)
                    .user(userResponse)
                    .issuedAt(Instant.now())
                    .build();

        } catch (Exception e) {
            // Increment failed login attempts
            user.incrementFailedLoginAttempts();
            userRepository.save(user);

            log.warn("Failed login attempt for user: {}", signInRequest.getEmail());
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    @Override
    @Transactional
    public ApiResponse<Void> verifyOtp(VerifyOtpRequest verifyOtpRequest) {
        // Verify OTP
        otpService.verifyOtp(
                verifyOtpRequest.getEmail(),
                verifyOtpRequest.getOtp(),
                OtpRecord.OtpType.EMAIL_VERIFICATION
        );

        // Activate user account
        User user = userRepository.findByEmail(verifyOtpRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setEmailVerified(true);
        user.setActive(true);
        userRepository.save(user);

        log.info("Email verified successfully: {}", verifyOtpRequest.getEmail());

        return ApiResponse.success("Email verified successfully. You can now sign in.");
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        // Validate refresh token format
        if (!tokenProvider.validateRefreshToken(refreshToken)) {
            throw new TokenException("Invalid refresh token");
        }

        // Get email from token
        String email = tokenProvider.getEmailFromRefreshToken(refreshToken);

        // Verify token exists in database and is valid
        RefreshToken storedToken = tokenService.verifyRefreshToken(refreshToken);

        // Get user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Generate new access token
        java.util.List<String> roles = user.getRoles().stream()
                .map(role -> "ROLE_" + role.getName())
                .collect(Collectors.toList());

        String newAccessToken = tokenProvider.generateAccessToken(email, roles);

        // Implement refresh token rotation
        String newRefreshToken = tokenProvider.generateRefreshToken(email);
        tokenService.rotateRefreshToken(storedToken, newRefreshToken);

        // Build response
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .emailVerified(user.isEmailVerified())
                .roles(roleNames)
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .build();

        log.info("Token refreshed successfully for user: {}", email);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .tokenType("Bearer")
                .expiresIn(tokenProvider.getAccessTokenExpiration() / 1000)
                .user(userResponse)
                .issuedAt(Instant.now())
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<Void> logout(String refreshToken) {
        if (refreshToken != null && !refreshToken.isEmpty()) {
            tokenService.revokeRefreshToken(refreshToken);
            log.info("User logged out successfully");
        }
        return ApiResponse.success("Logged out successfully");
    }

    @Override
    @Transactional
    public ApiResponse<Void> resendOtp(ResendOtpRequest resendOtpRequest) {
        String ipAddress = IpAddressUtil.getClientIpAddress(request);
        otpService.generateAndSendOtp(
                resendOtpRequest.getEmail(),
                OtpRecord.OtpType.EMAIL_VERIFICATION,
                ipAddress
        );

        return ApiResponse.success("OTP sent successfully");
    }
}