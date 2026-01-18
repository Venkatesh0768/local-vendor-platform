package org.localvendor.backend.vendor.repositories;

import org.localvendor.backend.vendor.model.Vendor;
import org.localvendor.backend.vendor.model.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VendorRepository extends JpaRepository<Vendor, UUID> {
    Optional<Vendor> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
    List<Vendor> findByVerificationStatus(VerificationStatus verificationStatus);
    Optional<Vendor> findByVendorId(UUID vendorId);
}