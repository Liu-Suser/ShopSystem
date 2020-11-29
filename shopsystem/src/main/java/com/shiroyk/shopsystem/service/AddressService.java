/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.entity.Address;
import com.shiroyk.shopsystem.entity.User;
import com.shiroyk.shopsystem.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public void save(Address address) {
        addressRepository.save(address);
    }

    public List<Address> findAllByUserId(User user) {
        return addressRepository.findAllByUserId(user);
    }

    public List<Address> findAllByUserIdAndNotDelete(User user) {
        return addressRepository.findAllByUserIdAndIsDelete(user, false);
    }

    public Optional<Address> findById(Long id) {
        return addressRepository.findById(id);
    }

    public Optional<Address> findAddressByUserDefault(User user) {
        return addressRepository.findAddressByUserIdAndIsDefaultTrue(user);
    }
}
