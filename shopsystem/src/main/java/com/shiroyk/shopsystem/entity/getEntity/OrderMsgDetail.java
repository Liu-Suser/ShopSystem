/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.entity.getEntity;

import com.shiroyk.shopsystem.entity.OrderDetail;

import java.math.BigDecimal;

public class OrderMsgDetail {
    private Long id;
    private ProductLite product;
    private CommentLite comment;
    private Integer quantity;
    private BigDecimal payment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductLite getProduct() {
        return product;
    }

    public void setProduct(ProductLite product) {
        this.product = product;
    }

    public CommentLite getComment() {
        return comment;
    }

    public void setComment(CommentLite comment) {
        this.comment = comment;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public OrderMsgDetail(OrderDetail orderDetail) {
        this.id = orderDetail.getId();
        this.product = new ProductLite(orderDetail.getProductId());
        if (orderDetail.getCommentId() != null) {
            this.comment = new CommentLite(orderDetail.getCommentId());
        } else {
            this.comment = null;
        }
        this.quantity = orderDetail.getQuantity();
        this.payment = orderDetail.getPayment();
    }
}
