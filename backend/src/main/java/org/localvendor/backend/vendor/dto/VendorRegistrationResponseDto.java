package org.localvendor.backend.vendor.dto;

import lombok.Builder;
import lombok.Data;
import org.localvendor.backend.vendor.model.VerificationStatus;

import java.util.UUID;

@Data
@Builder
public class VendorRegistrationResponseDto {

    private UUID vendorId;
    private String businessName;
    private VerificationStatus verificationStatus;
    private String message;
}
