package org.localvendor.authservice.repositories;

import org.localvendor.authservice.model.RefreshToken;
import org.localvendor.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    RefreshToken findAllByUserAndRevokedFalse(User user);
    Optional<RefreshToken> findByJti(String jti);
}