/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.dto.response;

import com.shiroyk.shopsystem.entity.Comment;

import java.time.LocalDateTime;

public class CommentLite {
    private Long id;

    private UserLite userId;

    private Long productId;

    private Integer rate;

    private String comment;

    private LocalDateTime createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserLite getUserId() {
        return userId;
    }

    public void setUserId(UserLite userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public CommentLite(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getUserId();
        this.productId = comment.getProductId();
        this.rate = comment.getRate();
        this.comment = comment.getComment();
        this.createTime = comment.getCreateTime();
    }
}
