package org.localvendor.vendorservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.localvendor.vendorservice.dto.VendorRequest;
import org.localvendor.vendorservice.dto.VendorResponse;
import org.localvendor.vendorservice.service.VendorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vendor")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @PostMapping("/apply")
    public ResponseEntity<VendorResponse> applyVendor(@Valid @RequestParam UUID userid , @RequestBody VendorRequest request) {
        VendorResponse response = vendorService.applyForVendorRole(userid ,request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<VendorResponse>> getAllVendors() {
        List<VendorResponse> response = vendorService.getAllVendors();
        return ResponseEntity.ok(response);
    }
}
