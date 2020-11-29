/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.repository;

import com.shiroyk.shopsystem.entity.Statistic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    @Query("SELECT statistic FROM Statistic statistic WHERE statistic.timestamp = CURRENT_DATE")
    Optional<Statistic> getTodayStatistic();

    List<Statistic> findAllBy(Pageable pageable);
}
