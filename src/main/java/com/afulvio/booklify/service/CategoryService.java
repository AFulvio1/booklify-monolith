package com.afulvio.booklify.service;

import java.lang.Long;

import com.afulvio.booklify.entity.Category;
import com.afulvio.booklify.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public void deleteCategoryById(int id) {
        categoryRepository.deleteById(id);
    }

    public Optional<Category> updateCategoryById(int id) {
        return categoryRepository.findById(id);
    }

    public Optional<Category> getCategoryById(int id) { return categoryRepository.findById(id); }
}
