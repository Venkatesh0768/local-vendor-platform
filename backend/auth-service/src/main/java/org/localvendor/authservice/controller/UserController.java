package org.localvendor.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.localvendor.authservice.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getProfile(
            org.springframework.security.core.Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(new ApiResponse(true,
                "Profile retrieved", Map.of("email", email)));
    }
}