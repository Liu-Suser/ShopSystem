/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.repository;

import com.shiroyk.shopsystem.entity.Category;
import com.shiroyk.shopsystem.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCategoryId(Category category, Pageable pageable);

    List<Product> findProductsByStatusIsTrue(Pageable pageable);

    Optional<Product> findProductByName(String name);

    List<Product> findProductsByStatusIsTrueAndSubtitleContains(String name);
}
