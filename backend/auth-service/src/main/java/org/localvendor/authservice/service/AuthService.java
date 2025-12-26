package org.localvendor.authservice.service;


import org.localvendor.authservice.dto.ApiResponse;
import org.localvendor.authservice.dto.LoginRequestDto;
import org.localvendor.authservice.dto.SignupRequestDto;

public interface  AuthService {
    public ApiResponse signup(SignupRequestDto requestDto);
    public ApiResponse login(LoginRequestDto requestDto);
}
