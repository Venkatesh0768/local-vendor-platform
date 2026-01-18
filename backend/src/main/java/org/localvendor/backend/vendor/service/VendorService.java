package org.localvendor.backend.vendor.service;

import org.localvendor.backend.vendor.dto.VendorRegistrationRequestDto;
import org.localvendor.backend.vendor.dto.VendorRegistrationResponseDto;

import java.util.UUID;

public interface VendorService {
    VendorRegistrationResponseDto registerAsVendor(UUID userId, VendorRegistrationRequestDto request);
}