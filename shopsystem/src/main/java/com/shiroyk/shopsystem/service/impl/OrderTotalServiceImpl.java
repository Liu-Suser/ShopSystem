package com.shiroyk.shopsystem.service.impl;

import com.github.pagehelper.PageHelper;
import com.shiroyk.shopsystem.constant.OrderStatus;
import com.shiroyk.shopsystem.entity.OrderTotal;
import com.shiroyk.shopsystem.mapper.OrderTotalMapper;
import com.shiroyk.shopsystem.service.OrderTotalService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderTotalServiceImpl implements OrderTotalService {
    private final OrderTotalMapper orderTotalMapper;

    public OrderTotalServiceImpl(OrderTotalMapper orderTotalMapper) {
        this.orderTotalMapper = orderTotalMapper;
    }

    @Override
    public void save(OrderTotal orderTotal) {
        orderTotal.setUpdateTime();
        orderTotalMapper.save(orderTotal);
    }

    @Override
    public Long getOrderCount() {
        return orderTotalMapper.count();
    }

    @Override
    public Long getOrderCountByUser(long uid) {
        return orderTotalMapper.countOrderTotalsByUserIdAndIsDeleteFalse(uid);
    }

    @Override
    public Long countOrderTotalsByStatus(OrderStatus status) {
        return orderTotalMapper.countOrderTotalsByStatus(OrderStatus.Ordered);
    }

    @Override
    public Optional<OrderTotal> findById(long id) {
        return orderTotalMapper.findById(id);
    }

    @Override
    public Optional<OrderTotal> findInfoById(long id) {
        return orderTotalMapper.findInfoById(id);
    }

    @Override
    public List<OrderTotal> findAllOrder(int num, int size) {
        PageHelper.startPage(num, size);
        return orderTotalMapper.findAll();
    }

    @Override
    public List<OrderTotal> findUserOrder(long uid, int num, int size) {
        PageHelper.startPage(num, size);
        return orderTotalMapper.findOrderTotalsByUserId(uid);
    }

    @Override
    public List<OrderTotal> findUserOrderAndDeleteFalse(long uid, int num, int size) {
        PageHelper.startPage(num, size);
        return orderTotalMapper.findOrderTotalsByUserIdAndIsDeleteFalse(uid);
    }

    @Override
    public List<OrderTotal> findOrderTotalsByStatus(OrderStatus status, int num, int size) {
        PageHelper.startPage(num, size);
        return orderTotalMapper.findOrderTotalsByStatus(status);
    }

    @Override
    public List<OrderTotal> findOrderTotalsByUserIdAndStatus(long uid, OrderStatus status, int num, int size) {
        PageHelper.startPage(num, size);
        return orderTotalMapper.findOrderTotalsByUserIdAndStatus(uid, status);
    }


}
