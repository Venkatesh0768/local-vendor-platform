package org.localvendor.authservice.repositories;

import org.localvendor.authservice.model.Role;
import org.localvendor.authservice.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleName(RoleType roleName);
}