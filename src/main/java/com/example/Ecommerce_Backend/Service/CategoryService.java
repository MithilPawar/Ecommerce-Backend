package com.example.Ecommerce_Backend.Service;

import com.example.Ecommerce_Backend.Exception.CustomExceptionHandler.*;
import com.example.Ecommerce_Backend.Model.Category;
import com.example.Ecommerce_Backend.Repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category does not exist"));
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(long id, Category category) {
        Category category1 = getCategoryById(id);
        category1.setName(category.getName());
        category1.setDescription(category.getDescription());
        return categoryRepository.save(category1);
    }

    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }
}
