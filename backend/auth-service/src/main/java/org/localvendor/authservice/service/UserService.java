package org.localvendor.authservice.service;


import org.localvendor.authservice.dto.ApiResponse;
import org.localvendor.authservice.dto.ResetPasswordRequest;
import org.localvendor.authservice.dto.UserDto;

public interface UserService {

    //get user by email
    ApiResponse getUserByEmail(String email);

    //update user
    ApiResponse updateUser(UserDto userDto, String userId);

    //delete user
    void deleteUser(String userId);

    //get user by id
    ApiResponse getUserById(String userId);

    //get all users
    Iterable<UserDto> getAllUsers();

    //reset password
    ApiResponse resetPassword(ResetPasswordRequest request);


}