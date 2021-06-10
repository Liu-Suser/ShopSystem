package com.shiroyk.shopsystem.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Cart implements Serializable {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Boolean checked;
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime updateTime = LocalDateTime.now();
}
