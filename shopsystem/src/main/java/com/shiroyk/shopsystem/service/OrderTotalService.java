/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.constant.OrderStatus;
import com.shiroyk.shopsystem.entity.OrderTotal;
import com.shiroyk.shopsystem.entity.User;
import com.shiroyk.shopsystem.entity.getEntity.OrderMsg;
import com.shiroyk.shopsystem.entity.getEntity.OrderMsgDetail;
import com.shiroyk.shopsystem.repository.OrderDetailRepository;
import com.shiroyk.shopsystem.repository.OrderTotalRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderTotalService {
    private final OrderTotalRepository orderTotalRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderTotalService(OrderTotalRepository orderTotalRepository,
                             OrderDetailRepository orderDetailRepository) {
        this.orderTotalRepository = orderTotalRepository;
        this.orderDetailRepository = orderDetailRepository;
    }


    public void save(OrderTotal orderTotal) {
        orderTotalRepository.save(orderTotal);
    }

    public Long getOrderCount() {
        return orderTotalRepository.count();
    }

    public Long getOrderCountByUser(User user) {
        return orderTotalRepository.countOrderTotalsByUserIdAndIsDeleteFalse(user);
    }

    public Long getOrderCountByWarehouse() {
        return orderTotalRepository.countOrderTotalsByStatus(OrderStatus.Payed) +
                orderTotalRepository.countOrderTotalsByStatus(OrderStatus.Shipped) +
                orderTotalRepository.countOrderTotalsByStatus(OrderStatus.Transit);
    }

    public Optional<OrderTotal> findById(Long id) {
        return orderTotalRepository.findById(id);
    }

    public Optional<OrderMsg> findOrderMsgById(Long id) {
        return orderTotalRepository.findById(id).map(this::orderTotalToOrderMsg);
    }

    public List<OrderMsg> findAllOrder(Pageable pageable) {
        return OrderMsgIterable(orderTotalRepository.findBy(pageable));
    }

    public List<OrderMsg> searchUserOrder(Pageable pageable, User user) {
        return OrderMsgIterable(
                orderTotalRepository.findOrderTotalsByUserId(pageable, user));
    }

    public List<OrderMsg> searchUserOrderAndDeleteFalse(Pageable pageable, User user) {
        return OrderMsgIterable(
                orderTotalRepository.findOrderTotalsByUserIdAndIsDeleteFalse(pageable, user));
    }


    public List<OrderMsg> findOrdersWarehouse(Pageable pageable) {
        return Stream.of(
                findOrderMsgByStatus(pageable, OrderStatus.Payed),
                findOrderMsgByStatus(pageable, OrderStatus.Shipped),
                findOrderMsgByStatus(pageable, OrderStatus.Transit))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<OrderMsg> searchUserOrderMsgByWarehouse(Pageable pageable, User user) {
        return Stream.of(
                searchUserOrderMsgByStatus(pageable, user, OrderStatus.Payed),
                searchUserOrderMsgByStatus(pageable, user, OrderStatus.Shipped),
                searchUserOrderMsgByStatus(pageable, user, OrderStatus.Transit))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<OrderMsg> findOrderMsgByStatus(Pageable pageable, OrderStatus status) {
        return OrderMsgIterable(
                orderTotalRepository.findOrderTotalsByStatus(pageable, status));
    }

    public List<OrderMsg> searchUserOrderMsgByStatus(Pageable pageable, User user, OrderStatus status) {
        return OrderMsgIterable(
                orderTotalRepository.findOrderTotalsByUserIdAndStatus(pageable, user, status));
    }

    private OrderMsg orderTotalToOrderMsg(OrderTotal total) {
        return new OrderMsg(total, orderDetailRepository
                .findOrderDetailByOrderIdId(total.getId())
                .stream().map(OrderMsgDetail::new)
                .collect(Collectors.toList()));
    }

    private List<OrderMsg> OrderMsgIterable(List<OrderTotal> totalList) {
        return totalList.stream().map(this::orderTotalToOrderMsg).collect(Collectors.toList());
    }
}
