package org.localvendor.authservice.repository;


import org.localvendor.authservice.entity.SecurityAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SecurityAuditLogRepository extends JpaRepository<SecurityAuditLog, Long> {
    List<SecurityAuditLog> findByUserIdAndCreatedAtBetween(Long userId, Instant start, Instant end);
    List<SecurityAuditLog> findByEventTypeAndCreatedAtAfter(String eventType, Instant after);
}