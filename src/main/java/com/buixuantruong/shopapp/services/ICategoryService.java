package com.buixuantruong.shopapp.services;

import com.buixuantruong.shopapp.dtos.CategoryDTO;
import com.buixuantruong.shopapp.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);

    Category getCategoryById(long id);

    List<Category> getAllCategories();

    Category updateCategory(CategoryDTO categoryDTO, long categoryId);

    void deleteCategory(long id);
}
