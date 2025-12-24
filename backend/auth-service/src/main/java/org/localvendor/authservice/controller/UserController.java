package org.localvendor.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.localvendor.authservice.dto.ApiResponse;
import org.localvendor.authservice.model.RoleType;
import org.localvendor.authservice.service.AuthService;
import org.localvendor.authservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getProfile(
            org.springframework.security.core.Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(new ApiResponse(true,
                "Profile retrieved", Map.of("email", email)));
    }


    @GetMapping("/exists/{userId}")
    public boolean userExists(@PathVariable UUID userId) {
        return userService.userExists(userId);
    }


    @PostMapping("/{userId}/add-role")
    public void addRoleToUser(@PathVariable UUID userId, @RequestParam RoleType roleName) {
        userService.addRoleToUser(userId, roleName);
    }

}