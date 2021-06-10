package com.shiroyk.shopsystem.entity;

import com.shiroyk.shopsystem.dto.response.CateGoryDto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Category implements Serializable {

    private Long id;
    private String name;
    private Boolean status;
    private Long parentId;
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime updateTime = LocalDateTime.now();

    public CateGoryDto toCateGoryDto() {
        return new CateGoryDto(id, name, status, parentId);
    }
}
