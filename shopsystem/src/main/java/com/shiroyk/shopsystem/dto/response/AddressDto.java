package com.shiroyk.shopsystem.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddressDto {
    private Long id;
    private Long userId;
    private String name;
    private String phone;
    private String address;
    private Boolean isDefault;
    private Boolean isDelete;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public AddressDto() {
    }

    public AddressDto(Long id, String name, String phone, String address, Boolean isDefault) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isDefault = isDefault;
    }
}
