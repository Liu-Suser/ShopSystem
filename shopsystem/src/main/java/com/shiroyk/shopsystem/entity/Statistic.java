/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class Statistic implements Serializable {

    private Long id;
    private Integer newOrder = 0;
    private Integer cancelOrder = 0;
    private Integer completeOrder = 0;
    private LocalDate timestamp = LocalDate.now();

    public void updateNewOrder() {
        this.newOrder++;
    }

    public void updateCancelOrder() {
        this.cancelOrder++;
    }

    public void updateCompleteOrder() {
        this.completeOrder++;
    }

    public String getTimestampFormat() {
        return timestamp.getMonth().getValue() + "/" + timestamp.getDayOfMonth();
    }

}
