package com.shiroyk.shopsystem.service.impl;

import com.shiroyk.shopsystem.entity.Cart;
import com.shiroyk.shopsystem.entity.User;
import com.shiroyk.shopsystem.mapper.CartMapper;
import com.shiroyk.shopsystem.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private final CartMapper cartMapper;

    public CartServiceImpl(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    @Override
    public void save(Cart cart) {
        cartMapper.save(cart);
    }

    @Override
    public void delete(long id) {
        cartMapper.delete(id);
    }

    @Override
    public Optional<Cart> findById(long id) {
        return cartMapper.findById(id);
    }

    @Override
    public List<Cart> findCartByUserId(long uid) {
        return cartMapper.findCartsByUserId(uid);
    }
}
