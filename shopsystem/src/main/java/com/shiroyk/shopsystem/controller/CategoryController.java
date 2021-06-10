/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.controller;

import com.shiroyk.shopsystem.dto.response.APIResponse;
import com.shiroyk.shopsystem.dto.response.CateGoryDto;
import com.shiroyk.shopsystem.dto.response.ProductDto;
import com.shiroyk.shopsystem.exception.NotFoundResourceException;
import com.shiroyk.shopsystem.service.CategoryService;
import com.shiroyk.shopsystem.entity.Category;
import com.shiroyk.shopsystem.entity.Product;
import com.shiroyk.shopsystem.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    
    /** 
    * @Description: 获取所有分类
    * @return: 分类列表
    */ 
    @GetMapping()
    public APIResponse<List<CateGoryDto>> getAllCategory() {
        return APIResponse.ok(categoryService.findAll()
                .stream().map(Category::toCateGoryDto)
                .collect(Collectors.toList()));
    }
    
    /** 
    * @Description: 获取单个分类
    * @Param: [categoryId] 
    * @return: 分类响应
    */ 
    @GetMapping("/{categoryId}")
    public APIResponse<List<CateGoryDto>> getCategoryByName(@PathVariable Long categoryId) {
        return APIResponse.ok(categoryService.findCategoriesByPid(categoryId).stream()
                .map(Category::toCateGoryDto)
                .collect(Collectors.toList()));
    }
    
    /** 
    * @Description: 获取分类下的商品列表
    * @Param: [categoryId, page, size] 
    * @return: 商品列表
    */ 
    @GetMapping("/{categoryId}/product")
    public APIResponse<List<ProductDto>> getProductByCategory(@PathVariable Long categoryId,
                                                              @RequestParam(required = false, defaultValue = "0", value="p") Integer page,
                                                              @RequestParam(required = false, defaultValue = "10", value="s") Integer size) {
        return categoryService.findById(categoryId).map(category ->
                    APIResponse.ok(productService.findAllByCategoryId(category.getId(), page, size)
                            .stream().map(Product::toProductDtoS)
                            .collect(Collectors.toList())))
                .orElseThrow(NotFoundResourceException::new);
    }

}
