package org.localvendor.vendorservice.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.localvendor.vendorservice.model.Status;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendorRequest {

    @NotBlank(message = "Shop name cannot be blank" )
    @Size(max = 100, message = "Shop name cannot exceed 100 characters")
    private String shopName;

    @NotBlank(message = "Category cannot be blank")
    private String category;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @NotBlank(message = "State cannot be blank")
    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    @NotBlank(message = "Zip code cannot be blank")
    @Size(max = 20, message = "Zip code cannot exceed 20 characters")
    private String zipCode;

    private Double longitude;
    private Double latitude;

}
