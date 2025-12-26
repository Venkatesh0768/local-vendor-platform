package org.localvendor.authservice.service;

import org.localvendor.authservice.dto.request.ForgotPasswordRequest;
import org.localvendor.authservice.dto.request.ResetPasswordRequest;
import org.localvendor.authservice.dto.response.ApiResponse;

public interface PasswordService {
    ApiResponse<Void> forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
    ApiResponse<Void> resetPassword(ResetPasswordRequest resetPasswordRequest);
}