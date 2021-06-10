package com.shiroyk.shopsystem.service.impl;

import com.github.pagehelper.PageHelper;
import com.shiroyk.shopsystem.entity.Statistic;
import com.shiroyk.shopsystem.mapper.StatisticMapper;
import com.shiroyk.shopsystem.service.StatisticService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatisticServiceImpl implements StatisticService {
    private final StatisticMapper statisticMapper;

    public StatisticServiceImpl(StatisticMapper statisticMapper) {
        this.statisticMapper = statisticMapper;
    }

    @Override
    public void save(Statistic statistic) {
        statisticMapper.save(statistic);
    }

    @Override
    public Optional<Statistic> getTodayStatistic() {
        return statisticMapper.getTodayStatistic();
    }

    @Override
    public List<Statistic> findAll(int num, int size) {
        PageHelper.startPage(num, size);
        return statisticMapper.findAll();
    }
}
