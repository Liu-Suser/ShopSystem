/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.component;

import com.shiroyk.shopsystem.constant.RabbitConstants;
import com.shiroyk.shopsystem.constant.StatisticEnum;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatisticSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessage(StatisticEnum statistic, final long delayTimes){
        amqpTemplate.convertAndSend(RabbitConstants.ORDER_EXCHANGE, RabbitConstants.ORDER_STATISTIC_KEY, statistic, message -> {
            message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
            return message;
        });
    }
}
