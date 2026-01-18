package org.localvendor.backend.product.service;

import org.localvendor.backend.product.dto.CategoryListItemDto;
import org.localvendor.backend.product.dto.CategoryResponseDto;
import org.localvendor.backend.product.dto.CreateCategoryRequestDto;
import org.localvendor.backend.product.dto.UpdateCategoryRequestDto;
import org.localvendor.backend.product.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
     CategoryResponseDto createCategory(UUID userId, CreateCategoryRequestDto request);
     Category getCategoryById(UUID categoryId);
     List<Category> getAllCategoryByVendorId(UUID vendorId);
     void deleteCategory(UUID userId, UUID categoryId);
     CategoryResponseDto updateCategory(UUID userId, UUID categoryId, UpdateCategoryRequestDto request);
     Page<CategoryListItemDto> listCategories(UUID userId, Boolean isActive, Pageable pageable);
}
