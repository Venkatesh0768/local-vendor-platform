package org.localvendor.backend.vendor.dto.vendorDto;


import lombok.Builder;
import lombok.Data;
import org.localvendor.backend.vendor.model.VendorType;
import org.localvendor.backend.vendor.model.VerificationStatus;


import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class VendorResponseDto {

    private UUID vendorId;
    private String businessName;
    private VendorType vendorType;
    private VerificationStatus verificationStatus;
    private Boolean isActive;
    private BigDecimal ratingAvg;
    private Integer totalReviews;
}