package org.localvendor.vendorservice.repositories;

import org.localvendor.vendorservice.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VendorRepository extends JpaRepository<Vendor, UUID> {
}