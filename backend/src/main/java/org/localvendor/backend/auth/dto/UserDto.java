package org.localvendor.backend.auth.dto;

import lombok.*;
import org.localvendor.backend.auth.model.Provider;
import org.localvendor.backend.auth.model.Role;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String image;
    private String lastName;
    private Provider provider;
    private boolean isEnabled;
    private Set<Role> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
