package org.localvendor.authservice.service;


import org.localvendor.authservice.dto.request.*;
import org.localvendor.authservice.dto.response.*;

public interface AuthService {
    ApiResponse<Void> signUp(SignUpRequest signUpRequest);
    AuthResponse signIn(SignInRequest signInRequest);
    ApiResponse<Void> verifyOtp(VerifyOtpRequest verifyOtpRequest);
    ApiResponse<Void> resendOtp(ResendOtpRequest resendOtpRequest);
    AuthResponse refreshToken(String refreshToken);
    ApiResponse<Void> logout(String refreshToken);
}