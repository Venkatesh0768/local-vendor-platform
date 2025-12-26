package org.localvendor.authservice.dto.response;

import lombok.*;
import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private boolean emailVerified;
    private Set<String> roles;
    private Instant lastLoginAt;
    private Instant createdAt;
}