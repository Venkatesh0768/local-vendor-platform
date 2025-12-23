package org.localvendor.authservice.dto;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private UUID id;
    private String phoneNumber;
    private String email;
    private String firstName;
    private String lastName;
    private boolean emailVerified;
    private Set<String> roles;
}