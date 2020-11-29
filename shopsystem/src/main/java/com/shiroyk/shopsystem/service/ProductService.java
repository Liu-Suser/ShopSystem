/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.entity.Category;
import com.shiroyk.shopsystem.repository.ProductRepository;
import com.shiroyk.shopsystem.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Long getProductCount() {
        return productRepository.count();
    }

    public List<Product> findAll(Pageable pageable) {
        return productRepository.findProductsByStatusIsTrue(pageable);
    }

    public List<Product> findAllByCategoryId(Category category, Pageable pageable) {
        return productRepository.findAllByCategoryId(category, pageable);
    }

    public Optional<Product> findProductByName(String name) {
        return productRepository.findProductByName(name);
    }

    public List<Product> searchProducts(String name) {
        return productRepository.findProductsByStatusIsTrueAndSubtitleContains(name);
    }
}
