/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.controller;

import com.shiroyk.shopsystem.entity.Comment;
import com.shiroyk.shopsystem.entity.Image;
import com.shiroyk.shopsystem.entity.Product;
import com.shiroyk.shopsystem.service.AdvertService;
import com.shiroyk.shopsystem.service.CommentService;
import com.shiroyk.shopsystem.service.FileService;
import com.shiroyk.shopsystem.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    Logger logger = LoggerFactory.getLogger(ProductController.class);
    private static final Integer PAGESIZE = 12;
    private final ProductService productService;
    private final CommentService commentService;
    private final FileService fileService;
    private final AdvertService advertService;

    public ProductController(ProductService productService,
                             CommentService commentService,
                             FileService fileService,
                             AdvertService advertService) {
        this.productService = productService;
        this.commentService = commentService;
        this.fileService = fileService;
        this.advertService = advertService;
    }

    @GetMapping("/pageSize")
    public Long getPageSize() {
        return productService.getProductCount();
    }

    @GetMapping()
    public List<Product> getProductList(@RequestParam(required = false, defaultValue = "0", value="page") Integer page) {
        Pageable pageable = PageRequest.of(page, PAGESIZE);
        return productService.findAll(pageable);
    }

    @GetMapping("/banner")
    public Image[] getBanner() {
        return advertService.getAdvert().getImage();
    }

    @GetMapping("/search")
    public List<Product> searchProduct(@RequestParam("product") String product) {
        return productService.searchProducts(product);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Long productId) {
        return productService.findById(productId)
                .map(product -> ResponseEntity.ok().body(product))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{productId}/comment")
    public List<Comment> getProductComment(@PathVariable Long productId) {
        return commentService.findCommentByProductId(productId);
    }

    @GetMapping("/image/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        try {
            Resource resource = fileService.loadImageAsResource(fileName);

            if (resource == null) {
                return ResponseEntity.notFound().build();
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
            return ResponseEntity.notFound().build();
        }
    }
}
