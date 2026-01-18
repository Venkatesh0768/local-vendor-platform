package org.localvendor.backend.vendor.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "vendor_location",
        indexes = {
                @Index(name = "idx_vendor_location_vendor_id", columnList = "vendor_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "location_id", updatable = false, nullable = false)
    private UUID locationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(name = "address_line", nullable = false)
    private String addressLine;

    @Column(name = "area")
    private String area;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "pincode", nullable = false)
    private String pincode;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.isPrimary = Boolean.TRUE;
    }


}
