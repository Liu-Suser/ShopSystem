package com.shiroyk.shopsystem.mapper;

import com.shiroyk.shopsystem.entity.Statistic;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface StatisticMapper {

    int save(Statistic statistic);

    Optional<Statistic> getTodayStatistic();

    List<Statistic> findAll();
}
