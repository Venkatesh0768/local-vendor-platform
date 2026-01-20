package org.localvendor.backend.admin.service;

import org.localvendor.backend.admin.dto.AdminVendorListPageResponseDto;

public interface AdminVendorQueryService {

    AdminVendorListPageResponseDto getAllVendors(
            String status,
            Boolean active,
            int page,
            int size
    );
}
