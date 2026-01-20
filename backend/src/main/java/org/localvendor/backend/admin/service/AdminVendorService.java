package org.localvendor.backend.admin.service;

import org.localvendor.backend.admin.dto.AdminVendorActionResponseDto;
import org.localvendor.backend.admin.dto.AdminVendorDetailsResponseDto;
import org.localvendor.backend.admin.dto.AdminVendorListResponseDto;
import org.localvendor.backend.vendor.model.Vendor;

import java.util.List;
import java.util.UUID;

public interface AdminVendorService {
    AdminVendorActionResponseDto approveVendor(UUID vendorId);
    AdminVendorActionResponseDto rejectVendor(UUID vendorId, String reason);
    public List<AdminVendorListResponseDto> getAllVendorForVerification();
    AdminVendorDetailsResponseDto getVendorDetails(UUID vendorId);
}
