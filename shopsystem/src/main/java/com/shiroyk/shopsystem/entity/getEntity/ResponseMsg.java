/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.entity.getEntity;

import org.springframework.http.HttpStatus;

public class ResponseMsg {
    HttpStatus status;
    String msg;

    public int getStatus() {
        return status.value();
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResponseMsg(HttpStatus status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
