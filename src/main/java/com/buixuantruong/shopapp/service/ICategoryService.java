package com.buixuantruong.shopapp.service;

import com.buixuantruong.shopapp.dto.CategoryDTO;
import com.buixuantruong.shopapp.model.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);

    Category getCategoryById(long id);

    List<Category> getAllCategories();

    Category updateCategory(CategoryDTO categoryDTO, long categoryId);

    void deleteCategory(long id);
}
