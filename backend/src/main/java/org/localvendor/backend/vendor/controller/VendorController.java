package org.localvendor.backend.vendor.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.localvendor.backend.helper.UserPrincipal;
import org.localvendor.backend.security.CustomUserDetailService;
import org.localvendor.backend.vendor.dto.VendorRegistrationRequestDto;
import org.localvendor.backend.vendor.dto.VendorRegistrationResponseDto;
import org.localvendor.backend.vendor.service.impl.VendorServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vendor")
@RequiredArgsConstructor
public class VendorController {

    private final VendorServiceImpl vendorService;


    @PostMapping("/register")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<VendorRegistrationResponseDto> registerVendorPartner(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody VendorRegistrationRequestDto request
    ) {
        if (user == null) {
            throw new AccessDeniedException("Unauthorized");
        }
        return ResponseEntity.ok(vendorService.registerAsVendor(user.getUser().getId(), request));
    }


}
