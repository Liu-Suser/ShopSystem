package com.shiroyk.shopsystem.entity;

import com.shiroyk.shopsystem.dto.response.AddressDto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Address implements Serializable {

    private Long id;
    private Long userId;
    private String name;
    private String phone;
    private String address;
    private Boolean isDefault;
    private Boolean isDelete = false;
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime updateTime = LocalDateTime.now();

    public AddressDto toAddressDtoS() {
        return new AddressDto(id, name, phone, address, isDefault);
    }

    public void setUpdateTime() {
        updateTime = LocalDateTime.now();
    }
}
