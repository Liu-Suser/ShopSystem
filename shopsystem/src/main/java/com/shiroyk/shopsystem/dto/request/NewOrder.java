/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class NewOrder {
    Long aid;
    List<Detail> details;

    @Data
    public static class Detail {
        Long pid;
        Long quantity;
    }

}
