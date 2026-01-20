package org.localvendor.backend.admin.service.impl;


import org.localvendor.backend.admin.dto.AdminProfileResponse;
import org.localvendor.backend.admin.service.AdminProfileService;
import org.localvendor.backend.helper.UserPrincipal;
import org.springframework.stereotype.Service;

@Service
public class AdminProfileServiceImpl implements AdminProfileService {

    @Override
    public AdminProfileResponse getProfile(UserPrincipal principal) {
        return AdminProfileResponse.builder()
                .userId(principal.getUser().getId())
                .name(principal.getUser().getFirstName()+ " " + principal.getUser().getLastName())
                .email(principal.getUser().getEmail())
                .build();
    }
}
