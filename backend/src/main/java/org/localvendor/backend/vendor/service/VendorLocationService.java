package org.localvendor.backend.vendor.service;

import org.localvendor.backend.vendor.dto.UpdateVendorLocationRequestDto;

import java.util.UUID;

public interface VendorLocationService {
     void updateLiveLocation(UUID userId, UpdateVendorLocationRequestDto request);
}
