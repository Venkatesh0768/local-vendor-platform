package org.localvendor.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.localvendor.authservice.dto.ApiResponse;
import org.localvendor.authservice.model.RoleType;
import org.localvendor.authservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse> getDashboard() {
        return ResponseEntity.ok(new ApiResponse(true,
                "Admin dashboard data", Map.of("role", "ADMIN")));
    }


}