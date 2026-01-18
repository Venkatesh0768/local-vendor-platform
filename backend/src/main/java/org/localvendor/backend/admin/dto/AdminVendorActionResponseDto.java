package org.localvendor.backend.admin.dto;
import lombok.Builder;
import lombok.Data;
import org.localvendor.backend.vendor.model.VerificationStatus;

import java.util.UUID;

@Data
@Builder
public class AdminVendorActionResponseDto {

    private UUID vendorId;
    private VerificationStatus verificationStatus;
    private String message;
}