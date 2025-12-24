package org.localvendor.authservice.service;

import lombok.RequiredArgsConstructor;
import org.localvendor.authservice.model.Role;
import org.localvendor.authservice.model.RoleType;
import org.localvendor.authservice.model.User;
import org.localvendor.authservice.repositories.RoleRepository;
import org.localvendor.authservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Boolean userExists(UUID userId) {
        return userRepository.existsById(userId);
    }


    public void addRoleToUser(UUID userId , RoleType roleName) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("User not found.")
        );

        Role role = roleRepository.findByName(roleName).orElseThrow(
                () -> new IllegalStateException("Role not found.")
        );


        if (user.getRoles().contains(role)) {
            return; // idempotent
        }

        user.getRoles().add(role);
        user.setEnabled(true);
        userRepository.save(user);
    }

}
