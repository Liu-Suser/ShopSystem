package com.shiroyk.shopsystem.service.impl;

import com.shiroyk.shopsystem.entity.Category;
import com.shiroyk.shopsystem.mapper.CategoryMapper;
import com.shiroyk.shopsystem.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public void save(Category category) {
        categoryMapper.save(category);
    }

    @Override
    public List<Category> findAll() {
        return categoryMapper.findAll();
    }

    @Override
    public List<Category> findCategoriesByPid(Long pid) {
        return categoryMapper.findCategoriesByParentId(pid);
    }

    @Override
    public Optional<Category> findCategoryByName(String name) {
        return categoryMapper.findCategoryByName(name);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryMapper.findById(id);
    }
}
