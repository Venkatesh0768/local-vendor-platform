package org.localvendor.backend.vendor.service;

import org.localvendor.backend.vendor.dto.NearbyVendorCategoryResponse;
import org.localvendor.backend.vendor.dto.NearbyVendorResponse;

import java.util.List;

public interface VendorSearchService {
    List<NearbyVendorResponse> findNearby(
            double lat,
            double lng,
            double radiusKm
    );

    List<NearbyVendorCategoryResponse> findNearbyByCategory(
            double lat,
            double lng,
            double radiusKm,
            String category
    );
}