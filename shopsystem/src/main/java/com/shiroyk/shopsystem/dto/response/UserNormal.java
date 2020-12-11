/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.dto.response;

import com.shiroyk.shopsystem.constant.UserRole;
import com.shiroyk.shopsystem.entity.User;

import java.time.LocalDateTime;

public class UserNormal extends UserLite {

    private String phone;

    private UserRole role;

    private LocalDateTime createTime;

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
        super(user);
        this.phone = user.getPhone();
        this.role = user.getRoleName();
        this.createTime = user.getCreateTime();
    }
}
