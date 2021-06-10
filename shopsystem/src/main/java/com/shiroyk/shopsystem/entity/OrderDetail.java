package com.shiroyk.shopsystem.entity;

import com.shiroyk.shopsystem.dto.response.OrderInfoDto;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDetail implements Serializable {
    private Long id;
    private Long orderId;
    private Long productId;
    private Product product;
    private Long commentId;
    private Integer quantity;
    private BigDecimal payment;
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime updateTime = LocalDateTime.now();

    public OrderInfoDto.OrderDetailDto toOrderDetailDto() {
        return new OrderInfoDto.OrderDetailDto(id, productId, commentId, quantity, product);
    }
}
