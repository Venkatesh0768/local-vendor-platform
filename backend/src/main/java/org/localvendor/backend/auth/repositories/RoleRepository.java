package org.localvendor.backend.auth.repositories;


import org.localvendor.backend.auth.model.Role;
import org.localvendor.backend.auth.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleName(RoleType roleName);
}