/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.constant;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserRole implements BaseEnum {
    Admin(0b00),
    Service(0b01),
    Warehouse(0b10),
    Normal(0b11);

    private final int role;

    UserRole(int role) {
        this.role = role;
    }

    @Override
    public int getValue() {
        return role;
    }
}
