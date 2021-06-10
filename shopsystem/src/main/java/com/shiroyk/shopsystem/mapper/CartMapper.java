package com.shiroyk.shopsystem.mapper;

import com.shiroyk.shopsystem.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface CartMapper {
    int save(Cart cart);

    int delete(Long id);

    Optional<Cart> findById(Long id);

    List<Cart> findCartsByUserId(long uid);
}
