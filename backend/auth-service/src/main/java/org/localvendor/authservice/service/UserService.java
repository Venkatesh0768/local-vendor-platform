package org.localvendor.authservice.service;


import org.localvendor.authservice.dto.response.UserResponse;

public interface UserService {
    UserResponse getCurrentUser();
    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);
    void deleteUser(Long id);
}