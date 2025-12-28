package org.localvendor.authservice.dto;

import lombok.*;
import org.localvendor.authservice.model.Provider;
import org.localvendor.authservice.model.Role;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
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
