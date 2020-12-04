/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.entity.getEntity;


import com.shiroyk.shopsystem.entity.User;

public class UserLite {
    private Long id;

    private String username;

    private String nickname;

    private Integer userpoint;

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
        return userpoint;
    }

    public void setUserPoint(Integer userpoint) {
        this.userpoint = userpoint;
    }

    public UserLite(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.userpoint = user.getUserPoint();
    }
}
