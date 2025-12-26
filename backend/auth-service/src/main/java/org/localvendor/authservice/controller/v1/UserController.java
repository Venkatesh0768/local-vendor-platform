package org.localvendor.authservice.controller.v1;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.localvendor.authservice.dto.response.ApiResponse;
import org.localvendor.authservice.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User management APIs")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Fetch user details and build response
        // Implementation omitted for brevity

        return ResponseEntity.ok(ApiResponse.success("User profile retrieved", null));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update user profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile() {
        // Implementation for profile update
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", null));
    }

    @DeleteMapping("/account")
    @Operation(summary = "Delete user account")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> deleteAccount() {
        // Implementation for account deletion
        return ResponseEntity.ok(ApiResponse.success("Account deleted successfully"));
    }
}