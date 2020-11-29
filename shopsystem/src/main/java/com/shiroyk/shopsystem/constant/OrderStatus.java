/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.constant;

public enum OrderStatus {
    Ordered(0b000),
    Cancel(0b001),
    Payed(0b010),
    Shipped(0b011),
    Transit(0b100),
    Received(0b101),
    Completed(0b110),
    AfterSale(0b111);

    private final int status;

    OrderStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

}
