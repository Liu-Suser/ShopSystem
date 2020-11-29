/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.entity.getEntity;

import com.shiroyk.shopsystem.constant.OrderStatus;
import com.shiroyk.shopsystem.entity.OrderTotal;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class OrderMsg {
    private Long id;
    private UserLite user;
    private AddressLite address;
    private OrderStatus status;
    private Boolean isDelete;
    private BigDecimal price;
    private String payMethod;
    private LocalDateTime paymentTime;
    private String express;
    private String logistics;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<OrderMsgDetail> details;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserLite getUser() {
        return user;
    }

    public void setUser(UserLite user) {
        this.user = user;
    }

    public AddressLite getAddress() {
        return address;
    }

    public void setAddress(AddressLite address) {
        this.address = address;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean delete) {
        isDelete = delete;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getLogistics() {
        return logistics;
    }

    public void setLogistics(String logistics) {
        this.logistics = logistics;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public List<OrderMsgDetail> getDetails() {
        return details;
    }

    public void setDetails(List<OrderMsgDetail> details) {
        this.details = details;
    }

    public OrderMsg(OrderTotal orderTotal, List<OrderMsgDetail> detailList) {
        this.id = orderTotal.getId();
        this.user = orderTotal.getUserId();
        this.address = new AddressLite(orderTotal.getAddressId());
        this.status = orderTotal.getStatus();
        this.isDelete = orderTotal.getDelete();
        this.price = orderTotal.getPrice();
        this.payMethod = orderTotal.getPayMethod();
        this.paymentTime = orderTotal.getPaymentTime();
        this.express = orderTotal.getExpress();
        this.logistics = orderTotal.getLogistics();
        this.createTime = orderTotal.getCreateTime();
        this.updateTime = orderTotal.getUpdateTime();
        this.details = detailList;
    }
}
