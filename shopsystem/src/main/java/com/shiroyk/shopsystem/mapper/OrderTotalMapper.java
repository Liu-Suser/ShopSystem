package com.shiroyk.shopsystem.mapper;

import com.shiroyk.shopsystem.constant.OrderStatus;
import com.shiroyk.shopsystem.entity.OrderTotal;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface OrderTotalMapper {
    int save(OrderTotal orderTotal);

    Optional<OrderTotal> findById(long id);

    Optional<OrderTotal> findInfoById(long id);

    List<OrderTotal> findAll();

    Long count();

    Long countOrderTotalsByUserIdAndIsDeleteFalse(long uid);

    Long countOrderTotalsByStatus(OrderStatus status);

    List<OrderTotal> findOrderTotalsByStatus(OrderStatus status);

    List<OrderTotal> findOrderTotalsByUserId(long uid);

    List<OrderTotal> findOrderTotalsByUserIdAndIsDeleteFalse(long uid);

    List<OrderTotal> findOrderTotalsByUserIdAndStatus(long uid, OrderStatus status);
}
