package org.localvendor.authservice.repositories;

import jakarta.validation.constraints.NotBlank;
import org.localvendor.authservice.model.Role;
import org.localvendor.authservice.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}