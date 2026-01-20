package org.localvendor.backend.product.controller;

import lombok.RequiredArgsConstructor;
import org.localvendor.backend.helper.UserPrincipal;
import org.localvendor.backend.product.dto.ProductImageResponseDto;
import org.localvendor.backend.product.service.ProductImageService;
import org.localvendor.backend.product.service.impl.ImageUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vendor/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('VENDOR')")
public class VendorProductImageController {

    private final ImageUploadService imageUploadService;
    private final ProductImageService productImageService;

    @PostMapping("/{productId}/images")
    public ResponseEntity<?> uploadImage(
            Authentication auth,
            @PathVariable UUID productId,
            @RequestParam("file") MultipartFile file
    ) {
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

        Map uploadResult = imageUploadService.uploadImage(file, "product_images");

        productImageService.addImage(
                principal.getUser().getId(),
                productId,
                uploadResult.get("secure_url").toString(),
                uploadResult.get("public_id").toString()
        );

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<?> deleteImage(
            Authentication auth,
            @PathVariable UUID imageId
    ) {
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        productImageService.deleteImage(principal.getUser().getId(), imageId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/images/{imageId}/primary")
    public ResponseEntity<?> setPrimary(
            Authentication auth,
            @PathVariable UUID imageId
    ) {
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        productImageService.setPrimaryImage(principal.getUser().getId(), imageId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<List<ProductImageResponseDto>> getProductImages(
            @PathVariable UUID productId
    ) {
        return ResponseEntity.ok(
                productImageService.getImagesByProductId(productId)
        );
    }
}
