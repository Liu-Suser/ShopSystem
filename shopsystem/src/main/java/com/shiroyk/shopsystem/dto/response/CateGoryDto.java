package com.shiroyk.shopsystem.dto.response;

import lombok.Data;

@Data
public class CateGoryDto {
    private Long id;
    private String name;
    private Boolean status;
    private Long parentId;

    public CateGoryDto(Long id, String name, Boolean status, Long parentId) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.parentId = parentId;
    }
}
