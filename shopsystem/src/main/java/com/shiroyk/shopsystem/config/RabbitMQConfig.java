/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.config;

import com.shiroyk.shopsystem.constant.RabbitConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(RabbitConstants.ORDER_EXCHANGE);
    }

    @Bean
    public Queue queueStatus() {
        return new Queue(RabbitConstants.ORDER_STATUS_QUEUE, true);
    }

    @Bean
    public Queue queueStatistic() {
        return new Queue(RabbitConstants.ORDER_STATISTIC_QUEUE, true);
    }

    @Bean
    public Binding bindingStatus(@Qualifier("queueStatus") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitConstants.ORDER_STATUS_KEY);
    }

    @Bean
    public Binding bindingStatistic(@Qualifier("queueStatistic") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitConstants.ORDER_STATISTIC_KEY);
    }
}
