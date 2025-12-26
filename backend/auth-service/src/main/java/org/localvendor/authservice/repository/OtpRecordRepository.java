package org.localvendor.authservice.repository;

import org.localvendor.authservice.entity.OtpRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRecordRepository extends JpaRepository<OtpRecord, Long> {

    Optional<OtpRecord> findTopByEmailAndOtpTypeAndUsedFalseOrderByCreatedAtDesc(
            String email,
            OtpRecord.OtpType otpType
    );

    Optional<OtpRecord> findTopByEmailAndOtpTypeOrderByCreatedAtDesc(
            String email,
            OtpRecord.OtpType otpType
    );

    List<OtpRecord> findByEmailAndOtpTypeAndUsedFalse(
            String email,
            OtpRecord.OtpType otpType
    );

    long countByEmailAndOtpTypeAndCreatedAtAfter(
            String email,
            OtpRecord.OtpType otpType,
            Instant createdAfter
    );

    @Modifying
    @Query("DELETE FROM OtpRecord o WHERE o.expiresAt < :currentTime OR o.used = true")
    void cleanupExpiredAndUsedOtps(Instant currentTime);
}