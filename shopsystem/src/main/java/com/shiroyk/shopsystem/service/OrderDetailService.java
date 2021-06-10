/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.entity.OrderDetail;

import java.util.List;
import java.util.Optional;

public interface OrderDetailService {

    void save(OrderDetail orderDetail);

    void insertAll(List<OrderDetail> orderDetail);

    Optional<OrderDetail> findById(Long id);

    List<OrderDetail> findOrderDetailByOrderId(Long id);

    List<OrderDetail> findOrderDetailAndProductByOrderId(Long id);

}
