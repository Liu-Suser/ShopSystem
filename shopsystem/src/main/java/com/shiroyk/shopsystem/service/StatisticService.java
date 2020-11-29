/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.entity.Statistic;
import com.shiroyk.shopsystem.repository.StatisticRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatisticService {
    private final StatisticRepository statisticRepository;

    public StatisticService(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    public void save(Statistic statistic) {
        statisticRepository.save(statistic);
    }

    public Optional<Statistic> getTodayStatistic() {
        return statisticRepository.getTodayStatistic();
    }

    public List<Statistic> findAll(Pageable pageable) {
        return statisticRepository.findAllBy(pageable)
                .stream()
                .sorted(Comparator.comparing(Statistic::getTimestamp))
                .collect(Collectors.toList());
    }
}
