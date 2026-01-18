package org.localvendor.backend.vendor.repositories;

import org.localvendor.backend.vendor.model.VendorLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VendorLocationRepository extends JpaRepository<VendorLocation, UUID> {
    List<VendorLocation> findByVendor_VendorId(UUID vendorId);
}