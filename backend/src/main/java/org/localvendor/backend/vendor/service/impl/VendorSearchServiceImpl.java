package org.localvendor.backend.vendor.service.impl;

import lombok.RequiredArgsConstructor;
import org.localvendor.backend.vendor.dto.NearbyVendorCategoryResponse;
import org.localvendor.backend.vendor.dto.NearbyVendorResponse;
import org.localvendor.backend.vendor.repositories.VendorLocationRepository;
import org.localvendor.backend.vendor.service.NearbyVendorProjection;
import org.localvendor.backend.vendor.service.VendorSearchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorSearchServiceImpl implements VendorSearchService {

    private final VendorLocationRepository locationRepository;


    @Override
    public List<NearbyVendorResponse> findNearby(
            double lat,
            double lng,
            double radiusKm
    ) {
        return locationRepository.findNearbyRaw(lat, lng, radiusKm)
                .stream()
                .map(row -> new NearbyVendorResponse(
                        row[0].toString(),                // HEX(vendor_id)
                        (String) row[1],                  // businessName
                        (String) row[2],                  // vendorType
                        (Double) row[3],                  // latitude
                        (Double) row[4],                  // longitude
                        ((Number) row[5]).doubleValue()   // distanceKm
                ))
                .toList();
    }

    @Override
    public List<NearbyVendorCategoryResponse> findNearbyByCategory(double lat, double lng, double radiusKm, String category) {
        return locationRepository.findNearbyByCategoryRaw(lat, lng, radiusKm, category)
                .stream()
                .map(r -> new NearbyVendorCategoryResponse(
                        r[0].toString(),                  // vendorId (HEX)
                        (String) r[1],                    // businessName
                        (String) r[2],                    // vendorType
                        (String) r[3],                    // categoryName
                        (Double) r[4],                    // latitude
                        (Double) r[5],                    // longitude
                        ((Number) r[6]).doubleValue()     // distanceKm
                ))
                .toList();
    }
}