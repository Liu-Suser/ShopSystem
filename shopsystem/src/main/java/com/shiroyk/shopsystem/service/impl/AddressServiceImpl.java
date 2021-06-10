package com.shiroyk.shopsystem.service.impl;

import com.shiroyk.shopsystem.entity.Address;
import com.shiroyk.shopsystem.entity.User;
import com.shiroyk.shopsystem.mapper.AddressMapper;
import com.shiroyk.shopsystem.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    @Override
    public void save(Address address) {
        addressMapper.save(address);
    }

    @Override
    public List<Address> findAllByUserId(long uid) {
        return addressMapper.findAllByUserId(uid);
    }

    @Override
    public List<Address> findAllByUserIdAndNotDelete(long uid) {
        return addressMapper.findAllByUserIdAndIsDelete(uid, false);
    }

    @Override
    public Optional<Address> findById(Long id) {
        return addressMapper.findById(id);
    }

    @Override
    public Address get(Long id) {
        return this.findById(id).orElse(null);
    }

    @Override
    public Optional<Address> findAddressByUserIdAndIsDefault(long uid) {
        return addressMapper.findAddressByUserIdAndIsDefault(uid);
    }
}
