package org.localvendor.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.localvendor.authservice.dto.ApiResponse;
import org.localvendor.authservice.dto.OtpRequest;
import org.localvendor.authservice.dto.SignupRequestDto;
import org.localvendor.authservice.service.impl.AuthServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1//auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;


    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody SignupRequestDto requestDto) {
        ApiResponse response = authService.signup(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(@RequestBody OtpRequest request) {
        ApiResponse response = authService.verifyOtp(request);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/resend-otp/{email}")
    public ResponseEntity<ApiResponse> resendOtp(@PathVariable String email) {
        ApiResponse response = authService.resendOtp(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
