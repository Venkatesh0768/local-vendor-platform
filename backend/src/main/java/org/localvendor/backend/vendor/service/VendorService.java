package org.localvendor.backend.vendor.service;

import org.localvendor.backend.auth.model.User;
import org.localvendor.backend.vendor.dto.VendorRegistrationRequestDto;
import org.localvendor.backend.vendor.dto.VendorRegistrationResponseDto;
import org.localvendor.backend.vendor.dto.vendorDto.VendorResponseDto;
import org.localvendor.backend.vendor.model.Vendor;

import java.util.UUID;

public interface VendorService {
    VendorRegistrationResponseDto registerAsVendor(UUID userId, VendorRegistrationRequestDto request);
    UUID getVendorIdByUser(User user);
    VendorResponseDto getMyVendorProfile(UUID userId);
    VendorResponseDto getVendorById(UUID vendorId);
}