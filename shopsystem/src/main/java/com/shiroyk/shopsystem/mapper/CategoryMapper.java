package com.shiroyk.shopsystem.mapper;

import com.shiroyk.shopsystem.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface CategoryMapper {
    int save(Category category);

    List<Category> findAll();

    Optional<Category> findById(Long id);

    List<Category> findCategoriesByParentId(long parentId);

    Optional<Category> findCategoryByName(String name);
}
