/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.component;

import com.shiroyk.shopsystem.constant.RabbitConstants;
import com.shiroyk.shopsystem.constant.StatisticEnum;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class StatisticSender {

    private final AmqpTemplate amqpTemplate;

    public StatisticSender(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendMessage(StatisticEnum statistic, final long delayTimes){
        amqpTemplate.convertAndSend(RabbitConstants.ORDER_EXCHANGE, RabbitConstants.ORDER_STATISTIC_KEY, statistic, message -> {
            message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
            return message;
        });
    }
}
