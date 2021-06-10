package com.shiroyk.shopsystem.mapper;

import com.shiroyk.shopsystem.entity.Address;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface AddressMapper {

    int save(Address address);

    Optional<Address> findById(Long id);

    List<Address> findAllByUserId(long uid);

    List<Address> findAllByUserIdAndIsDelete(long uid, boolean isDelete);

    Optional<Address> findAddressByUserIdAndIsDefault(long uid);
}
