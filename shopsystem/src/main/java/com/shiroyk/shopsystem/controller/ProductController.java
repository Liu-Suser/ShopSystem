/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.controller;

import com.shiroyk.shopsystem.dto.response.APIResponse;
import com.shiroyk.shopsystem.dto.response.CommentDto;
import com.shiroyk.shopsystem.dto.response.ProductDto;
import com.shiroyk.shopsystem.entity.Comment;
import com.shiroyk.shopsystem.entity.Image;
import com.shiroyk.shopsystem.entity.Product;
import com.shiroyk.shopsystem.exception.NotFoundResourceException;
import com.shiroyk.shopsystem.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    private final UserService userService;
    private final FileService fileService;
    private final AdvertService advertService;

    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             CommentService commentService,
                             UserService userService,
                             FileService fileService,
                             AdvertService advertService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.commentService = commentService;
        this.userService = userService;
        this.fileService = fileService;
        this.advertService = advertService;
    }
    
    /** 
    * @Description: 获取商品数量
    * @return: 数量
    */ 
    @GetMapping("/count")
    public APIResponse<Long> getPageSize() {
        return APIResponse.ok(productService.getProductCount());
    }
    
    /** 
    * @Description: 获取商品列表
    * @Param: [page, size] 
    * @return: 商品列表
    */ 
    @GetMapping()
    public APIResponse<List<ProductDto>> getProductList(@RequestParam(required = false, defaultValue = "0", value="p") Integer page,
                                                        @RequestParam(required = false, defaultValue = "10", value="s") Integer size) {
        return APIResponse.ok(productService.findAll(page, size).stream()
                .map(Product::toProductDtoS)
                .collect(Collectors.toList()));
    }
    
    @GetMapping("/banner")
    public APIResponse<Image> getBanner() {
        return APIResponse.ok(advertService.getAdvert().getImage());
    }
    
    /** 
    * @Description: 搜索商品
    * @Param: [name] 
    * @return: 商品列表
    */ 
    @GetMapping("/search")
    public APIResponse<List<ProductDto>> searchProduct(@RequestParam("name") String name) {
        log.info(productService.searchProducts(name).toString());
        return APIResponse.ok(productService.searchProducts(name).stream()
                .map(Product::toProductDtoS).collect(Collectors.toList()));
    }
    
    /** 
    * @Description: 获取单个商品
    * @Param: [productId] 
    * @return: 商品响应
    */ 
    @GetMapping("/{productId}")
    public APIResponse<ProductDto> getProduct(@PathVariable Long productId) {
        return productService.findById(productId)
            .map(product ->
                categoryService.findById(product.getCategoryId())
                .map(category ->
                    APIResponse.ok(product.toProductDto(category.toCateGoryDto())))
                .orElse(APIResponse.ok(product.toProductDto(null))))
            .orElseThrow(NotFoundResourceException::new);
    }
    
    /** 
    * @Description: 获取商品的评论
    * @Param: [productId] 
    * @return: 评论列表
    */ 
    @GetMapping("/{productId}/comment")
    public APIResponse<List<CommentDto>> getProductComment(@PathVariable Long productId) {
        return APIResponse.ok(commentService.findCommentByProductId(productId)
                .stream().map(comment -> comment.toCommentDto(userService.get(comment.getUserId()).toUserDtoS()))
                .collect(Collectors.toList()));
    }
    
    /** 
    * @Description: 获取商品的图片
    * @Param: [fileName, request] 
    * @return: 图片响应
    */ 
    @GetMapping("/image/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        try {
            Resource resource = fileService.loadImageAsResource(fileName);

            if (resource == null) {
                throw new NotFoundResourceException();
            }
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if(contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new NotFoundResourceException();
        }
    }
}
