package org.localvendor.authservice.repositories;

import org.localvendor.authservice.model.EmailOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailOtpRepository extends JpaRepository<EmailOtp, Long> {
    Optional<EmailOtp> findByEmailAndOtpCodeAndVerifiedFalse(String email, String otpCode);
    void deleteByEmail(String email);
}
