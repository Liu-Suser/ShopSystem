package com.shiroyk.shopsystem.mapper;

import com.shiroyk.shopsystem.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface OrderDetailMapper {
    int save(OrderDetail orderDetail);

    void insertAll(List<OrderDetail> orderDetail);

    Optional<OrderDetail> findById(Long id);

    List<OrderDetail> findOrderDetailByOrderId(long id);

    List<OrderDetail> findOrderDetailAndProductByOrderId(long id);
}
