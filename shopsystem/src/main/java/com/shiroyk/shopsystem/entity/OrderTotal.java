package com.shiroyk.shopsystem.entity;

import com.shiroyk.shopsystem.constant.OrderStatus;
import com.shiroyk.shopsystem.dto.response.OrderInfoDto;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderTotal implements Serializable {

    private Long id;
    private Long userId;
    private User user;
    private Long addressId;
    private Address address;
    private OrderStatus status;
    private Boolean isDelete;
    private BigDecimal price;
    private String payMethod;
    private LocalDateTime paymentTime;
    private String express;
    private String logistics;
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime updateTime = LocalDateTime.now();

    public OrderInfoDto toOrderInfoDtoS() {
        return new OrderInfoDto(id, status, price, paymentTime, createTime);
    }

    public OrderInfoDto toOrderInfoDto(List<OrderDetail> orderDetails) {
        return new OrderInfoDto(id, userId, user,
                addressId, address, orderDetails,
                status, price, payMethod,
                express, logistics, paymentTime,
                createTime);
    }

    public void setUpdateTime() {
        updateTime = LocalDateTime.now();
    }

    public void setPaymentTime() {
        paymentTime = LocalDateTime.now();
    }

    public boolean isPayed() {
        return OrderStatus.Payed.equals(status);
    }

    public boolean isShipped() {
        return OrderStatus.Shipped.equals(status);
    }

    public boolean isOrdered() {
        return OrderStatus.Ordered.equals(status);
    }

    public boolean isTransit() {
        return OrderStatus.Transit.equals(status);
    }

    public boolean isCompleted() {
        return OrderStatus.Completed.equals(status);
    }
}
