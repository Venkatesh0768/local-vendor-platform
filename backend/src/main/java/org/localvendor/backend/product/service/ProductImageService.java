package org.localvendor.backend.product.service;

import org.localvendor.backend.product.dto.ProductImageResponseDto;
import org.localvendor.backend.product.model.ProductImage;

import java.util.List;
import java.util.UUID;

public interface ProductImageService {

    void addImage(UUID vendorUserId, UUID productId, String imageUrl, String publicId);

    List<ProductImage> getProductImages(UUID productId);
    List<ProductImageResponseDto> getImagesByProductId(UUID productId);

    void setPrimaryImage(UUID vendorUserId, UUID imageId);

    void deleteImage(UUID vendorUserId, UUID imageId);
}