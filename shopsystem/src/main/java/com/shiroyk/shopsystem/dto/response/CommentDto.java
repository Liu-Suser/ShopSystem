package com.shiroyk.shopsystem.dto.response;

import lombok.Data;

import java.util.Set;

@Data
public class CommentDto {
    private Long id;
    private Long userId;
    private Long productId;
    private UserDto user;
    private ProductDto product;
    private Integer rate;
    private Set<String> image;
    private String comment;

    public CommentDto(Long id, Long userId, UserDto user, Integer rate, String comment) {
        this.id = id;
        this.userId = userId;
        this.user = user;
        this.rate = rate;
        this.comment = comment;
    }

    public CommentDto(Long id, UserDto user, Integer rate, Set<String> image, String comment) {
        this.id = id;
        this.user = user;
        this.rate = rate;
        this.image = image;
        this.comment = comment;
    }

    public CommentDto(Long id, Long userId, Long productId, UserDto user, ProductDto product, Integer rate, Set<String> image, String comment) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.user = user;
        this.product = product;
        this.rate = rate;
        this.image = image;
        this.comment = comment;
    }
}
