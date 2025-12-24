package org.localvendor.vendorservice.controller;


import lombok.RequiredArgsConstructor;
import org.localvendor.vendorservice.model.Vendor;
import org.localvendor.vendorservice.service.VendorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/vendor")
@RequiredArgsConstructor
public class VendorAdminController {

    private final VendorService vendorService;

    @GetMapping
    public ResponseEntity<List<Vendor>> getAllPendingVendors() {
        List<Vendor> response = vendorService.getAllPendingVendors();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/approve")
    public ResponseEntity<String> approveVendor(@RequestParam UUID vendorId) {
        vendorService.approveVendor(vendorId);
        return ResponseEntity.ok("Vendor approved successfully");
    }

    @PostMapping("/reject")
    public ResponseEntity<String> rejectVendor(@RequestParam UUID vendorId) {
        vendorService.rejectVendor(vendorId);
        return ResponseEntity.ok("Vendor rejected successfully");
    }

}
