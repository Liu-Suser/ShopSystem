/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.controller;

import com.shiroyk.shopsystem.dto.response.CommonResponse;
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
    public CommonResponse<List<Category>> getAllCategory() {
        return CommonResponse.create(categoryService.findAll());
    }
    @GetMapping("/{categoryId}")
    public CommonResponse<List<Category>> getCategoryByName(@PathVariable Long categoryId) {
        return CommonResponse.create(categoryService.findCategoriesByPid(categoryId));
    }

    @GetMapping("/{categoryId}/product")
    public CommonResponse<List<Product>> getProductByCategory(@PathVariable Long categoryId, @RequestParam(required = false, defaultValue = "0", value="page") Integer page) {
        return categoryService.findById(categoryId).map(category -> {
            Pageable pageable = PageRequest.of(page,12);
            return CommonResponse.create(productService.findAllByCategoryId(category, pageable));
        }).orElseThrow(NotFoundResourceException::new);
    }

}
