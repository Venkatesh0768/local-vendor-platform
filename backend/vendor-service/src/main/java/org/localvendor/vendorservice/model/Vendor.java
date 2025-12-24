package org.localvendor.vendorservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.UUID;

@Entity(name = "vendors")
public class Vendor extends BaseModel {

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String shopName;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String zipCode;

    private Double longitude;
    private Double latitude;

    @Column(nullable = false)
    private Status status;
}
