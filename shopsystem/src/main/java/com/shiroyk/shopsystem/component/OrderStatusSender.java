/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.component;

import com.shiroyk.shopsystem.constant.RabbitConstants;
import com.shiroyk.shopsystem.entity.OrderTotal;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusSender {

    private final AmqpTemplate amqpTemplate;

    public OrderStatusSender(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendMessage(OrderTotal orderId, final long delayTimes){
        amqpTemplate.convertAndSend(RabbitConstants.ORDER_EXCHANGE, RabbitConstants.ORDER_STATUS_KEY, orderId, message -> {
            message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
            return message;
        });
    }
}
