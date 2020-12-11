/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.dto.response;

import com.shiroyk.shopsystem.dto.response.BaseResponse;
import org.springframework.http.HttpStatus;

public class ErrorResponse extends BaseResponse {
    public ErrorResponse(HttpStatus status, String msg) {
        super(status, msg);
    }
}
