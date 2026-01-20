package org.localvendor.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.localvendor.backend.admin.dto.AdminProfileResponse;
import org.localvendor.backend.admin.service.AdminProfileService;
import org.localvendor.backend.helper.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminProfileController {

    private final AdminProfileService adminProfileService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminProfileResponse> getProfile(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(adminProfileService.getProfile(principal));
    }
}