package org.localvendor.vendorservice.service;

import lombok.RequiredArgsConstructor;
import org.localvendor.vendorservice.client.AuthFeignClient;
import org.localvendor.vendorservice.dto.VendorRequest;
import org.localvendor.vendorservice.dto.VendorResponse;
import org.localvendor.vendorservice.model.RoleType;
import org.localvendor.vendorservice.model.Status;
import org.localvendor.vendorservice.model.Vendor;
import org.localvendor.vendorservice.repositories.VendorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;
    private final AuthFeignClient authFeignClient;


    @Transactional
    public VendorResponse applyForVendorRole(UUID userId, VendorRequest request) {

        if (vendorRepository.existsByUserId(userId)) {
            throw new IllegalStateException("Vendor application already exists for this user.");
        }
        Vendor vendor = Vendor.builder()
                .userId(userId)
                .shopName(request.getShopName())
                .category(request.getCategory())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .status(Status.PENDING)
                .build();

        Vendor savedVendor = vendorRepository.save(vendor);

        return mapToDto(savedVendor);
    }


    public List<Vendor> getAllPendingVendors() {
        return vendorRepository.findByStatus(Status.PENDING);
    }

    //Admin Contrloller will use this method
    @Transactional
    public void approveVendor(UUID vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalStateException("Vendor not found."));

        vendor.setStatus(Status.ACTIVE);
        vendorRepository.save(vendor);

        if (vendor.getStatus() == Status.ACTIVE) {
            authFeignClient.addRoleToUser(vendor.getUserId(), RoleType.ROLE_VENDOR);
        }
    }

    @Transactional
    public void rejectVendor(UUID vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalStateException("Vendor not found."));

        vendor.setStatus(Status.REJECTED);
        vendorRepository.save(vendor);
    }



    public List<VendorResponse> getAllVendors() {
        List<Vendor> vendors = vendorRepository.findAll();
        return vendors.stream()
                .map(this::mapToDto)
                .toList();
    }

    public VendorResponse mapToDto(Vendor vendor) {
        return VendorResponse.builder()
                .id(vendor.getId())
                .userId(vendor.getUserId())
                .shopName(vendor.getShopName())
                .category(vendor.getCategory())
                .address(vendor.getAddress())
                .city(vendor.getCity())
                .state(vendor.getState())
                .zipCode(vendor.getZipCode())
                .latitude(vendor.getLatitude())
                .longitude(vendor.getLongitude())
                .status(vendor.getStatus())
                .build();
    }


}
