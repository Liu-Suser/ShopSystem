/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.dto.response;

import org.springframework.http.HttpStatus;

public class BaseResponse {
    private HttpStatus status;
    private String msg;

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

    public BaseResponse(String msg) {
        this.status = HttpStatus.OK;
        this.msg = msg;
    }

    public BaseResponse(HttpStatus status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
