/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.dto.response;

import com.shiroyk.shopsystem.constant.UserRole;
import com.shiroyk.shopsystem.entity.JwtUser;

public class LoginResponse {
    private String username;
    private UserRole role;
    private String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRole() {
        return role.getRole();
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginResponse(JwtUser user, String token) {
        this.username = user.getUsername();
        this.role = user.getRole();
        this.token = token;
    }
}
