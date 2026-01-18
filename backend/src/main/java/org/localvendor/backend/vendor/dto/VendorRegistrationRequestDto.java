package org.localvendor.backend.vendor.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.localvendor.backend.vendor.model.VendorType;

@Data
public class VendorRegistrationRequestDto {

    @NotBlank
    private String businessName;

    @NotNull
    private VendorType vendorType;

    @Valid
    @NotNull
    private VendorLocationRequestDto location;
}
