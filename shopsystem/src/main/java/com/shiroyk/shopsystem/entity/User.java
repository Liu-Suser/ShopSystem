/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk
 */

package com.shiroyk.shopsystem.entity;

import com.shiroyk.shopsystem.constant.UserRole;
import com.shiroyk.shopsystem.dto.response.UserDto;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class User implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private Integer userpoint = 0;
    private String phone;
    private String question = "";
    private String answer = "";
    private UserRole role = UserRole.Normal;
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime updateTime = LocalDateTime.now();

    public List<SimpleGrantedAuthority> getRoleAuthority() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        String roleStr;
        switch (role) {
            case Admin:
                roleStr = "ROLE_ADMIN";
                break;
            case Service:
                roleStr = "ROLE_SERVICE";
                break;
            case Warehouse:
                roleStr = "ROLE_WAREHOUSE";
                break;
            default:
                roleStr = "ROLE_NORMAL";
        }
        authorities.add(new SimpleGrantedAuthority(roleStr));
        return authorities;
    }

    public void inUserpoint(int i) {
        userpoint += i / 100;
    }

    public UserDto toUserDtoS() {
        return new UserDto(id, username, nickname, userpoint);
    }

    public UserDto toUserDtoM() {
        return new UserDto(id, username, nickname, userpoint, phone);
    }
}
