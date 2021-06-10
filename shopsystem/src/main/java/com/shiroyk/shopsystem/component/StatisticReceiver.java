/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.component;

import com.shiroyk.shopsystem.constant.RabbitConstants;
import com.shiroyk.shopsystem.constant.StatisticEnum;
import com.shiroyk.shopsystem.entity.Statistic;
import com.shiroyk.shopsystem.service.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = RabbitConstants.ORDER_STATISTIC_QUEUE)
public class StatisticReceiver {

    private final StatisticService statisticService;

    public StatisticReceiver(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @RabbitHandler
    public void handle(StatisticEnum status){
        Statistic statistic = getStatistic();
        switch (status) {
            case newOrder:
                statistic.updateNewOrder();
                break;
            case CancelOrder:
                statistic.updateCancelOrder();
                break;
            case CompleteOrder:
                statistic.updateCompleteOrder();
                break;
        }
        statisticService.save(statistic);
    }

    private Statistic getStatistic() {
        return statisticService.getTodayStatistic().orElseGet(Statistic::new);
    }
}
