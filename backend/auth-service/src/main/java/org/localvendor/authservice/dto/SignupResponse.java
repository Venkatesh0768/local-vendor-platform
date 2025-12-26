package org.localvendor.authservice.dto;

import lombok.Builder;
import lombok.Data;
import org.localvendor.authservice.model.Role;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class SignupResponse {
    private UUID id;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
