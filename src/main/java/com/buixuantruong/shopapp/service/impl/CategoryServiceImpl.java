package com.buixuantruong.shopapp.service.impl;

import com.buixuantruong.shopapp.dto.ApiResponse;
import com.buixuantruong.shopapp.dto.CategoryDTO;
import com.buixuantruong.shopapp.exception.StatusCode;
import com.buixuantruong.shopapp.model.Category;
import com.buixuantruong.shopapp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements com.buixuantruong.shopapp.service.CategoryService {

    private final CategoryRepository categoryRepository;


    @Override
    public ApiResponse<Object> createCategory(CategoryDTO categoryDTO) {
        Category newCategory = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(categoryRepository.save(newCategory))
                .build();
    }

    @Override
    public ApiResponse<Object> getCategoryById(Long id) {
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(categoryRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Category not found")))
                .build();
    }

    @Override
    public ApiResponse<Object> getAllCategories() {
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(categoryRepository.findAll())
                .build();
    }

    @Override
    public ApiResponse<Object> updateCategory(CategoryDTO categoryDTO,
                                   Long categoryId) {
        Optional<Category> optionalCategory =categoryRepository.findById(categoryId);
        Category existingCategory = optionalCategory.get();
        existingCategory.setName(categoryDTO.getName());
        categoryRepository.save(existingCategory);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(existingCategory)
                .build();
    }

    @Override
    public ApiResponse<Object> deleteCategory(Long id) {
        categoryRepository.deleteById(id);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result("Category deleted successfully")
                .build();
    }
}
