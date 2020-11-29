/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.component;

import com.shiroyk.shopsystem.constant.RabbitConstants;
import com.shiroyk.shopsystem.constant.StatisticEnum;
import com.shiroyk.shopsystem.entity.Statistic;
import com.shiroyk.shopsystem.service.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitConstants.ORDER_STATISTIC_QUEUE)
public class StatisticReceiver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private StatisticService statisticService;

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
