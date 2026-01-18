package org.localvendor.backend.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.localvendor.backend.admin.dto.AdminVendorActionResponseDto;
import org.localvendor.backend.admin.service.impl.AdminVendorServiceImpl;
import org.localvendor.backend.vendor.dto.VendorRejectionRequestDto;
import org.localvendor.backend.vendor.model.Vendor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/admin/vendors")
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminVendorController {

    private final AdminVendorServiceImpl adminVendorService;

    @PutMapping("/{vendorId}/approve")
    public ResponseEntity<AdminVendorActionResponseDto> approveVendor(@PathVariable UUID vendorId) {
        return ResponseEntity.ok(adminVendorService.approveVendor(vendorId));
    }

    @PutMapping("/{vendorId}/reject")
    public ResponseEntity<AdminVendorActionResponseDto> rejectVendor(@PathVariable UUID vendorId, @Valid @RequestBody VendorRejectionRequestDto request) {
        return ResponseEntity.ok(adminVendorService.rejectVendor(vendorId, request.getReason()));
    }

    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendorForVerification(){
        return ResponseEntity.ok(adminVendorService.getAllVendorForVerification());
    }
}
