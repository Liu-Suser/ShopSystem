/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.entity.getEntity;

import com.shiroyk.shopsystem.constant.UserRole;
import com.shiroyk.shopsystem.entity.User;

import java.time.LocalDateTime;

public class UserNormal {
    private Long id;

    private String username;

    private String nickname;

    private Integer userPoint;

    private String phone;

    private UserRole role;

    private LocalDateTime createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(Integer userPoint) {
        this.userPoint = userPoint;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRole() {
        return role.getRole();
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public UserNormal(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.userPoint = user.getUserPoint();
        this.phone = user.getPhone();
        this.role = user.getRoleName();
        this.createTime = user.getCreateTime();
    }
}
