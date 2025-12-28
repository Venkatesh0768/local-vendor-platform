package org.localvendor.authservice.service;


import org.localvendor.authservice.dto.ApiResponse;
import org.localvendor.authservice.dto.LoginRequestDto;
import org.localvendor.authservice.dto.SignupRequestDto;

public interface  AuthService {
     ApiResponse signup(SignupRequestDto requestDto);
     ApiResponse login(LoginRequestDto requestDto);
}
