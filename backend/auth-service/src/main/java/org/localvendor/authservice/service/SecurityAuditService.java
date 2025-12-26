package org.localvendor.authservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.localvendor.authservice.entity.SecurityAuditLog;
import org.localvendor.authservice.repository.SecurityAuditLogRepository;
import org.localvendor.authservice.util.IpAddressUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityAuditService {

    private final SecurityAuditLogRepository auditLogRepository;
    private final HttpServletRequest request;
    private final ObjectMapper objectMapper;

    @Async
    public void logEvent(String eventType, Long userId, Map<String, Object> details) {
        try {
            String ipAddress = IpAddressUtil.getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");

            SecurityAuditLog auditLog = SecurityAuditLog.builder()
                    .userId(userId)
                    .eventType(eventType)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .details(objectMapper.writeValueAsString(details))
                    .createdAt(Instant.now())
                    .build();

            auditLogRepository.save(auditLog);

            log.info("Security event logged: {} for user: {}", eventType, userId);
        } catch (Exception e) {
            log.error("Failed to log security event", e);
        }
    }

    public enum EventType {
        LOGIN_SUCCESS,
        LOGIN_FAILED,
        SIGNUP,
        EMAIL_VERIFIED,
        PASSWORD_RESET,
        TOKEN_REFRESH,
        LOGOUT,
        ACCOUNT_LOCKED,
        SUSPICIOUS_ACTIVITY
    }
}
