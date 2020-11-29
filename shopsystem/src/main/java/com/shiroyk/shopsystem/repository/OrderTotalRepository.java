/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.repository;

import com.shiroyk.shopsystem.constant.OrderStatus;
import com.shiroyk.shopsystem.entity.OrderTotal;
import com.shiroyk.shopsystem.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTotalRepository extends JpaRepository<OrderTotal, Long> {

    List<OrderTotal> findBy(Pageable pageable);

    Long countOrderTotalsByUserIdAndIsDeleteFalse(User user);

    Long countOrderTotalsByStatus(OrderStatus status);

    List<OrderTotal> findOrderTotalsByStatus(Pageable pageable, OrderStatus status);

    List<OrderTotal> findOrderTotalsByUserId(Pageable pageable, User user);

    List<OrderTotal> findOrderTotalsByUserIdAndIsDeleteFalse(Pageable pageable, User user);

    List<OrderTotal> findOrderTotalsByUserIdAndStatus(Pageable pageable, User user, OrderStatus status);
}
