/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.constant;

public enum  StatisticEnum {
    newOrder(0),
    CancelOrder(1),
    CompleteOrder(2);

    private final int statistic;

    StatisticEnum(int i) {
        this.statistic = i;
    }

    public int getStatistic() {
        return statistic;
    }
}
