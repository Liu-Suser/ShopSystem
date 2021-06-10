package com.shiroyk.shopsystem.service.impl;

import com.shiroyk.shopsystem.entity.OrderDetail;
import com.shiroyk.shopsystem.mapper.OrderDetailMapper;
import com.shiroyk.shopsystem.service.OrderDetailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailMapper orderDetailMapper;

    public OrderDetailServiceImpl(OrderDetailMapper orderDetailMapper) {
        this.orderDetailMapper = orderDetailMapper;
    }

    @Override
    public void save(OrderDetail orderDetail) {
        orderDetailMapper.save(orderDetail);
    }

    @Override
    public void insertAll(List<OrderDetail> orderDetail) {
        orderDetailMapper.insertAll(orderDetail);
    }

    @Override
    public Optional<OrderDetail> findById(Long id) {
        return orderDetailMapper.findById(id);
    }

    @Override
    public List<OrderDetail> findOrderDetailByOrderId(Long id) {
        return orderDetailMapper.findOrderDetailByOrderId(id);
    }

    @Override
    public List<OrderDetail> findOrderDetailAndProductByOrderId(Long id) {
        return orderDetailMapper.findOrderDetailAndProductByOrderId(id);
    }
}
