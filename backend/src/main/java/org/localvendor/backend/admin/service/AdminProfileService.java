package org.localvendor.backend.admin.service;
import org.localvendor.backend.admin.dto.AdminProfileResponse;
import org.localvendor.backend.helper.UserPrincipal;

public interface AdminProfileService {
    AdminProfileResponse getProfile(UserPrincipal principal);
}