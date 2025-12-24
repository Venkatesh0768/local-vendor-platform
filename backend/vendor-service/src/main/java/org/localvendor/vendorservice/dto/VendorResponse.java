package org.localvendor.vendorservice.dto;

import jakarta.persistence.Column;
import lombok.*;
import org.localvendor.vendorservice.model.Status;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendorResponse {
    private UUID id;
    private UUID userId;
    private String shopName;
    private String category;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private Double longitude;
    private Double latitude;
    private Status status;

}
