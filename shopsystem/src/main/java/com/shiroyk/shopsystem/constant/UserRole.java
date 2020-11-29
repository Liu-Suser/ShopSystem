/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.constant;

public enum UserRole {
    Admin(0b00),
    Service(0b01),
    Warehouse(0b10),
    Normal(0b11);

    private final int role;

    UserRole(int role) {
        this.role = role;
    }

    public int getRole() {
        return role;
    }
}
