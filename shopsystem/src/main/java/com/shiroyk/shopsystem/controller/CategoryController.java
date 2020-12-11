/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.controller;

import com.shiroyk.shopsystem.dto.response.SuccessResponse;
import com.shiroyk.shopsystem.exception.NotFoundResourceException;
import com.shiroyk.shopsystem.service.CategoryService;
import com.shiroyk.shopsystem.entity.Category;
import com.shiroyk.shopsystem.entity.Product;
import com.shiroyk.shopsystem.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryController(CategoryService categoryService,
                              ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping()
    public SuccessResponse<List<Category>> getAllCategory() {
        return SuccessResponse.create(categoryService.findAll());
    }
    @GetMapping("/{categoryId}")
    public SuccessResponse<List<Category>> getCategoryByName(@PathVariable Long categoryId) {
        return SuccessResponse.create(categoryService.findCategoriesByPid(categoryId));
    }

    @GetMapping("/{categoryId}/product")
    public SuccessResponse<List<Product>> getProductByCategory(@PathVariable Long categoryId, @RequestParam(required = false, defaultValue = "0", value="page") Integer page) {
        return categoryService.findById(categoryId).map(category -> {
            Pageable pageable = PageRequest.of(page,12);
            return SuccessResponse.create(productService.findAllByCategoryId(category, pageable));
        }).orElseThrow(NotFoundResourceException::new);
    }

}
