/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.entity.Cart;

import java.util.List;
import java.util.Optional;

public interface CartService {

    void save(Cart cart);

    void delete(long id);

    Optional<Cart> findById(long id);

    List<Cart> findCartByUserId(long uid);
}
