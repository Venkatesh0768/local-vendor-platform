package org.localvendor.backend.admin.service.impl;

import lombok.RequiredArgsConstructor;
import org.localvendor.backend.admin.dto.AdminVendorActionResponseDto;
import org.localvendor.backend.admin.service.AdminVendorService;
import org.localvendor.backend.auth.model.Role;
import org.localvendor.backend.auth.model.RoleType;
import org.localvendor.backend.auth.model.User;
import org.localvendor.backend.auth.repositories.RoleRepository;
import org.localvendor.backend.auth.repositories.UserRepository;
import org.localvendor.backend.exception.UserNotFoundException;
import org.localvendor.backend.exception.vendor_exceptions.VendorAlreadyVerifiedException;
import org.localvendor.backend.exception.vendor_exceptions.VendorNotFoundException;
import org.localvendor.backend.vendor.model.Vendor;
import org.localvendor.backend.vendor.model.VerificationStatus;
import org.localvendor.backend.vendor.repositories.VendorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminVendorServiceImpl implements AdminVendorService {

    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    @Transactional
    public AdminVendorActionResponseDto approveVendor(UUID vendorId) {
        Vendor vendor = vendorRepository.findByUserId(vendorId).orElseThrow(
                ()-> new VendorNotFoundException("Vendor Is not Found by this id " + vendorId)
        );

        if (vendor.getVerificationStatus() == VerificationStatus.APPROVED){
            throw new VendorAlreadyVerifiedException("Vendor already approved");
        }

        //approve the vendor
        vendor.setVerificationStatus(VerificationStatus.APPROVED);
        vendor.setIsActive(true);
        vendorRepository.save(vendor);

        User user = userRepository.findById(vendor.getUserId()).orElseThrow(
                () -> new UserNotFoundException("User not found by this userid " +  vendor.getUserId())
        );

        Role userRole = roleRepository.findByRoleName(RoleType.ROLE_VENDOR)
                .orElseThrow(() -> new RuntimeException("ROLE VENDOR not found"));

        user.getRoles().add(userRole);


        return AdminVendorActionResponseDto.builder()
                .vendorId(vendor.getVendorId())
                .verificationStatus(vendor.getVerificationStatus())
                .message("Vendor approved and user promoted to VENDOR")
                .build();
    }

    @Override
    public AdminVendorActionResponseDto rejectVendor(UUID vendorId, String reason) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));

        if (vendor.getVerificationStatus() == VerificationStatus.APPROVED) {
            throw new IllegalStateException("Approved vendor cannot be rejected");
        }

        vendor.setVerificationStatus(VerificationStatus.REJECTED);
        vendor.setIsActive(false);
        vendorRepository.save(vendor);

        // Optional: store rejection reason in a separate table
        // vendorRejectionRepository.save(...)

        return AdminVendorActionResponseDto.builder()
                .vendorId(vendor.getVendorId())
                .verificationStatus(vendor.getVerificationStatus())
                .message("Vendor rejected. Reason: " + reason)
                .build();
    }


    @Override
    @Transactional
    public List<Vendor> getAllVendorForVerification(){
        return vendorRepository.findByVerificationStatus(VerificationStatus.PENDING);
    }

}
