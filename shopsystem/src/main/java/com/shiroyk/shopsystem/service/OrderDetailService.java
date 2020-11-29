/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.entity.OrderDetail;
import com.shiroyk.shopsystem.repository.OrderDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    public void save(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }

    public void save(List<OrderDetail> orderDetail) {
        orderDetailRepository.saveAll(orderDetail);
    }

    public Optional<OrderDetail> findById(Long id) {
        return orderDetailRepository.findById(id);
    }

    public List<OrderDetail> findOrderDetailByOrderId(Long id) {
        return orderDetailRepository.findOrderDetailByOrderIdId(id);
    }

}
