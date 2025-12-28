package org.localvendor.authservice.service.impl;


import lombok.RequiredArgsConstructor;
import org.localvendor.authservice.dto.ApiResponse;
import org.localvendor.authservice.dto.LoginRequestDto;
import org.localvendor.authservice.dto.SignupRequestDto;
import org.localvendor.authservice.dto.SignupResponse;
import org.localvendor.authservice.exception.EmailAlreadyExistException;
import org.localvendor.authservice.model.Role;
import org.localvendor.authservice.model.RoleType;
import org.localvendor.authservice.model.User;
import org.localvendor.authservice.repositories.RoleRepository;
import org.localvendor.authservice.repositories.UserRepository;
import org.localvendor.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpServiceImpl otpService;

    @Override
    @Transactional
    public ApiResponse signup(SignupRequestDto requestDto) {

        if (requestDto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required");
        }

        if (requestDto.getEmail() == null || requestDto.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }

        if (userRepository.existsByEmail(requestDto.getEmail().trim().toLowerCase())) {
            throw new EmailAlreadyExistException( "Email already registered");
        }

        String encode = null;
        if (requestDto.getPassword() != null || requestDto.getPassword().isBlank()) {
            encode = passwordEncoder.encode(requestDto.getPassword());
        }

        User user = User.builder()
                .email(requestDto.getEmail().trim().toLowerCase())
                .password(encode) //Hashing
                .phoneNumber(requestDto.getPhoneNumber())
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .image(requestDto.getImage())
                .build();

        // Assign default role
        Role userRole = roleRepository.findByRoleName(RoleType.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRoles(Set.of(userRole));

        //Save User
        User savedUser = userRepository.save(user);

        otpService.generateAndSendOtp(savedUser.getEmail());

        return new ApiResponse(true, "Registration successful. Please verify your email with OTP sent to " +
                user.getEmail(), null);

    }

    @Override
    public ApiResponse login(LoginRequestDto requestDto) {
        return null;
    }


}
