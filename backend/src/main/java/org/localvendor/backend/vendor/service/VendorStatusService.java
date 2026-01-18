package org.localvendor.backend.vendor.service;

import org.localvendor.backend.vendor.dto.VendorStatusRequestDto;
import org.localvendor.backend.vendor.dto.VendorStatusResponseDto;

import java.util.UUID;

public interface VendorStatusService {
    void updateStatus(UUID vendorId, VendorStatusRequestDto dto);
    VendorStatusResponseDto getStatus(UUID vendorId);
}