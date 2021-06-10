/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.component;

import com.shiroyk.shopsystem.constant.RabbitConstants;
import com.shiroyk.shopsystem.entity.OrderTotal;
import com.shiroyk.shopsystem.entity.Product;
import com.shiroyk.shopsystem.service.OrderDetailService;
import com.shiroyk.shopsystem.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = RabbitConstants.ORDER_STATUS_QUEUE)
public class OrderStatusReceiver {

    private final OrderDetailService orderDetailService;
    private final ProductService productService;

    public OrderStatusReceiver(OrderDetailService orderDetailService, ProductService productService) {
        this.orderDetailService = orderDetailService;
        this.productService = productService;
    }

    @RabbitHandler
    public void handle(OrderTotal orderTotal){
        switch (orderTotal.getStatus()) {
            case Cancel:
                cancelOrder(orderTotal.getId());
                log.info("Order cancel " + orderTotal.getId());
                break;
            case Completed:
                completedOrder(orderTotal.getId());
                log.info("Order completed " + orderTotal.getId());
                break;
        }
    }

    private void cancelOrder(Long orderId) {
        orderDetailService.findOrderDetailByOrderId(orderId).forEach(detail ->
            productService.findById(detail.getProductId()).ifPresent(product -> {
                product.increaseInventory(detail.getQuantity());
                productService.save(product);
        }));
    }

    private void completedOrder(Long orderId) {
        orderDetailService.findOrderDetailByOrderId(orderId).forEach(detail ->
            productService.findById(detail.getProductId()).ifPresent(product -> {
                product.updateSale(detail.getQuantity());
                productService.save(product);
        }));
    }
}
