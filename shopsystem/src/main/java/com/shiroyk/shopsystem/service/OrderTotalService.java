/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.constant.OrderStatus;
import com.shiroyk.shopsystem.entity.OrderTotal;
import com.shiroyk.shopsystem.entity.User;
import com.shiroyk.shopsystem.dto.response.OrderResponse;
import com.shiroyk.shopsystem.dto.response.OrderDetailResponse;
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

    public Optional<OrderResponse> findOrderResponseById(Long id) {
        return orderTotalRepository.findById(id).map(this::orderTotalToOrderResponse);
    }

    public List<OrderResponse> findAllOrder(Pageable pageable) {
        return OrderResponseIterable(orderTotalRepository.findBy(pageable));
    }

    public List<OrderResponse> searchUserOrder(Pageable pageable, User user) {
        return OrderResponseIterable(
                orderTotalRepository.findOrderTotalsByUserId(pageable, user));
    }

    public List<OrderResponse> searchUserOrderAndDeleteFalse(Pageable pageable, User user) {
        return OrderResponseIterable(
                orderTotalRepository.findOrderTotalsByUserIdAndIsDeleteFalse(pageable, user));
    }


    public List<OrderResponse> findOrdersWarehouse(Pageable pageable) {
        return Stream.of(
                findOrderResponseByStatus(pageable, OrderStatus.Payed),
                findOrderResponseByStatus(pageable, OrderStatus.Shipped),
                findOrderResponseByStatus(pageable, OrderStatus.Transit))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> searchUserOrderResponseByWarehouse(Pageable pageable, User user) {
        return Stream.of(
                searchUserOrderResponseByStatus(pageable, user, OrderStatus.Payed),
                searchUserOrderResponseByStatus(pageable, user, OrderStatus.Shipped),
                searchUserOrderResponseByStatus(pageable, user, OrderStatus.Transit))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> findOrderResponseByStatus(Pageable pageable, OrderStatus status) {
        return OrderResponseIterable(
                orderTotalRepository.findOrderTotalsByStatus(pageable, status));
    }

    public List<OrderResponse> searchUserOrderResponseByStatus(Pageable pageable, User user, OrderStatus status) {
        return OrderResponseIterable(
                orderTotalRepository.findOrderTotalsByUserIdAndStatus(pageable, user, status));
    }

    private OrderResponse orderTotalToOrderResponse(OrderTotal total) {
        return new OrderResponse(total, orderDetailRepository
                .findOrderDetailByOrderIdId(total.getId())
                .stream().map(OrderDetailResponse::new)
                .collect(Collectors.toList()));
    }

    private List<OrderResponse> OrderResponseIterable(List<OrderTotal> totalList) {
        return totalList.stream().map(this::orderTotalToOrderResponse).collect(Collectors.toList());
    }
}
