/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.constant.OrderStatus;
import com.shiroyk.shopsystem.entity.OrderTotal;

import java.util.List;
import java.util.Optional;

public interface OrderTotalService {

    void save(OrderTotal orderTotal);

    Long getOrderCount();

    Long getOrderCountByUser(long uid);

    Long countOrderTotalsByStatus(OrderStatus status);

    Optional<OrderTotal> findById(long id);

    Optional<OrderTotal> findInfoById(long id);

    List<OrderTotal> findAllOrder(int num, int size);

    List<OrderTotal> findUserOrder(long uid, int num, int size);

    List<OrderTotal> findUserOrderAndDeleteFalse(long uid, int num, int size);

    List<OrderTotal> findOrderTotalsByStatus(OrderStatus status, int num, int size);

    List<OrderTotal> findOrderTotalsByUserIdAndStatus(long uid, OrderStatus status, int num, int size);
}
