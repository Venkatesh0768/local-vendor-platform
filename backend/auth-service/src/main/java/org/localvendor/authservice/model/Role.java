package org.localvendor.authservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseModel {

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleType name;
}

