package org.localvendor.backend.auth.service;


import jakarta.servlet.http.HttpServletResponse;
import org.localvendor.backend.auth.dto.ApiResponse;
import org.localvendor.backend.auth.dto.LoginRequestDto;
import org.localvendor.backend.auth.dto.SignupRequestDto;


public interface  AuthService {
     ApiResponse signup(SignupRequestDto requestDto);
     ApiResponse login(LoginRequestDto requestDto , HttpServletResponse response);
}
