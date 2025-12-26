package org.localvendor.authservice.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "otp_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(name = "otp_hash", nullable = false)
    private String otpHash;

    @Column(name = "otp_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OtpType otpType;

    @Column(nullable = false)
    private int attempts = 0;

    @Column(name = "max_attempts")
    private int maxAttempts = 5;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Column(name = "is_used")
    private boolean used = false;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public boolean canAttempt() {
        return attempts < maxAttempts && !isExpired() && !used;
    }

    public void incrementAttempts() {
        this.attempts++;
    }

    public enum OtpType {
        EMAIL_VERIFICATION,
        PASSWORD_RESET
    }
}
