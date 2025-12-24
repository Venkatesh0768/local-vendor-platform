package org.localvendor.vendorservice.client;

import org.localvendor.vendorservice.model.RoleType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "AUTH-SERVICE-DEV", path = "/api/user")
public interface AuthFeignClient {

    @GetMapping("/exists/{userId}")
    boolean userExists(@PathVariable UUID userId);

    @PostMapping("/{userId}/add-role")
    void addRoleToUser(
            @PathVariable UUID userId,
            @RequestParam("roleName") RoleType roleName
    );
}