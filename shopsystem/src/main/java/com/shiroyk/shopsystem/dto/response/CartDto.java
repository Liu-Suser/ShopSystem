package com.shiroyk.shopsystem.dto.response;

import lombok.Data;

@Data
public class CartDto {
    private Long id;
    private Long userId;
    private Long productId;
    private UserDto user;
    private ProductDto product;
    private Integer quantity;
    private Boolean checked;
}
