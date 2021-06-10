package com.shiroyk.shopsystem.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class ProductDto {
    private Long id;
    private Long categoryId;
    private CateGoryDto cateGory;
    private String name;
    private String subtitle;
    private Set<String> image;
    private String detail;
    private BigDecimal price;
    private Integer sale;
    private Integer inventory;
    private Boolean status;

    public ProductDto(Long id, String name, String subtitle, Set<String> image, BigDecimal price, Integer sale, Integer inventory) {
        this.id = id;
        this.name = name;
        this.subtitle = subtitle;
        this.image = image;
        this.price = price;
        this.sale = sale;
        this.inventory = inventory;
    }

    public ProductDto(Long id, Long categoryId, CateGoryDto cateGory, String name, String subtitle, Set<String> image, String detail, BigDecimal price, Integer sale, Integer inventory, Boolean status) {
        this.id = id;
        this.categoryId = categoryId;
        this.cateGory = cateGory;
        this.name = name;
        this.subtitle = subtitle;
        this.image = image;
        this.detail = detail;
        this.price = price;
        this.sale = sale;
        this.inventory = inventory;
        this.status = status;
    }
}
