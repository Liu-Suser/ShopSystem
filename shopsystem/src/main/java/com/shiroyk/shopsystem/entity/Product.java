/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk
 */

package com.shiroyk.shopsystem.entity;

import com.shiroyk.shopsystem.dto.response.CateGoryDto;
import com.shiroyk.shopsystem.dto.response.ProductDto;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product implements Serializable {

    private Long id;
    private Long categoryId;
    private String name;
    private String subtitle;
    private Image image;
    private String detail;
    private BigDecimal price;
    private Integer sale = 0;
    private Integer inventory;
    private Boolean status;
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime updateTime = LocalDateTime.now();

    public ProductDto toProductDtoS() {
        return new ProductDto(id, name, subtitle, image.getNames(), price, sale, inventory);
    }

    public ProductDto toProductDto(CateGoryDto cateGoryDto) {
        return new ProductDto(id, categoryId, cateGoryDto, name, subtitle, image.getNames(), detail, price, sale, inventory, status);
    }

    public void reduceInventory(Integer quantity) {
        if (inventory > 0 && inventory > quantity)
            inventory -= quantity;
    }

    public void increaseInventory(Integer quantity) {
        if (inventory > 0 && inventory > quantity)
            inventory += quantity;
    }

    public void updateSale(Integer quantity) {
        if (sale > 0 && sale > quantity)
            sale += quantity;
    }
}
