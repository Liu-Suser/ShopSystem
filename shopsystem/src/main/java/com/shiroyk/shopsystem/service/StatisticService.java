/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.entity.Statistic;

import java.util.List;
import java.util.Optional;

public interface StatisticService {

    void save(Statistic statistic);

    Optional<Statistic> getTodayStatistic();

    List<Statistic> findAll(int num, int size);
}
