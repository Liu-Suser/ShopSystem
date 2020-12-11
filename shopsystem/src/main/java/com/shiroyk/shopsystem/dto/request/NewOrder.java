/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.dto.request;

import java.util.List;
import java.util.Map;

public class NewOrder {

    Long aid;
    List<Map<String, Long>> details;

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public List<Map<String, Long>> getDetails() {
        return details;
    }

    public void setDetails(List<Map<String, Long>> details) {
        this.details = details;
    }
}
