/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.component;

import com.shiroyk.shopsystem.constant.RabbitConstants;
import com.shiroyk.shopsystem.entity.OrderTotal;
import com.shiroyk.shopsystem.entity.Product;
import com.shiroyk.shopsystem.service.OrderDetailService;
import com.shiroyk.shopsystem.service.OrderTotalService;
import com.shiroyk.shopsystem.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitConstants.ORDER_STATUS_QUEUE)
public class OrderStatusReceiver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private OrderTotalService orderTotalService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ProductService productService;

    @RabbitHandler
    public void handle(OrderTotal orderTotal){
        switch (orderTotal.getStatus()) {
            case Cancel:
                cancelOrder(orderTotal.getId());
                logger.info("Order cancel " + orderTotal.getId());
                break;
            case Completed:
                completedOrder(orderTotal.getId());
                logger.info("Order completed " + orderTotal.getId());
                break;
        }
    }

    private void cancelOrder(Long orderId) {
        orderDetailService.findOrderDetailByOrderId(orderId).forEach(detail -> {
            Product product = detail.getProductId();
            product.plusStock(detail.getQuantity());
            productService.save(product);
        });
    }

    private void completedOrder(Long orderId) {
        orderDetailService.findOrderDetailByOrderId(orderId).forEach(detail -> {
            Product product = detail.getProductId();
            product.updateSale(detail.getQuantity());
            productService.save(product);
        });
    }
}
