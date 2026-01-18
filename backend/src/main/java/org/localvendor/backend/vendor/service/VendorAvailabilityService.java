package org.localvendor.backend.vendor.service;

import org.localvendor.backend.vendor.dto.VendorAvailabilityRequestDto;

import java.util.UUID;

public interface VendorAvailabilityService {
    void saveAvailability(UUID vendorId, VendorAvailabilityRequestDto dto);
}
