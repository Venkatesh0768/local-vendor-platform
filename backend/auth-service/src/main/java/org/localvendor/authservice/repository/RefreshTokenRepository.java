package org.localvendor.authservice.repository;

import org.localvendor.authservice.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findByUserIdAndRevokedFalse(Long userId);

    List<RefreshToken> findByRevokedFalseAndExpiresAtAfter(Instant currentTime);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :currentTime OR (rt.user.id = :userId AND rt.revoked = true)")
    void deleteExpiredTokens(Instant currentTime, Long userId);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true, rt.revokedAt = :revokedAt WHERE rt.user.id = :userId AND rt.revoked = false")
    void revokeAllUserTokens(Long userId, Instant revokedAt);
}