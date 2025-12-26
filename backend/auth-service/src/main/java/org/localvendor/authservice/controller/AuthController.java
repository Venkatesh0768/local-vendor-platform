package org.localvendor.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.localvendor.authservice.dto.ApiResponse;
import org.localvendor.authservice.dto.SignupRequestDto;
import org.localvendor.authservice.service.impl.AuthServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody SignupRequestDto requestDto){
        ApiResponse response = authService.signup(requestDto);
        return new ResponseEntity<>(response , HttpStatus.CREATED);
    }
}
