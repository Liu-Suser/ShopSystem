/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.entity.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {

    void save(Address address);

    Optional<Address> findById(Long id);

    Address get(Long id);

    List<Address> findAllByUserId(long uid);

    List<Address> findAllByUserIdAndNotDelete(long uid);

    Optional<Address> findAddressByUserIdAndIsDefault(long uid);
}
