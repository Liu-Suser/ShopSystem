/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    void save(Category category);

    List<Category> findAll();

    Optional<Category> findById(Long id);

    List<Category> findCategoriesByPid(Long pid);

    Optional<Category> findCategoryByName(String name);
}
