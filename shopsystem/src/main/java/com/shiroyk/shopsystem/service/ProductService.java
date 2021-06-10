/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.entity.Product;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {

    void save(Product product);

    void updateInventory(List<Product> product);

    Optional<Product> findById(Long id);

    Long getProductCount();

    List<Product> findAll(int num, int size);

    Map<Long, Product> findProductInventoryMapByIdList(List<Long> idList);

    List<Product> findAllByCategoryId(long categoryId, int num, int size);

    Optional<Product> findProductByName(String name);

    List<Product> searchProducts(String name);
}
