/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.repository;

import com.shiroyk.shopsystem.entity.Address;
import com.shiroyk.shopsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByUserId(User user);

    List<Address> findAllByUserIdAndIsDelete(User user,boolean isDelete);

    Optional<Address> findAddressByUserIdAndIsDefaultTrue(User user);
}
