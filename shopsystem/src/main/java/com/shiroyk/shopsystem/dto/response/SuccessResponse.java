/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.dto.response;

public class SuccessResponse<T> extends BaseResponse {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> SuccessResponse<T> create(T data) {
        return new SuccessResponse<>("Success", data);
    }

    public static <T> SuccessResponse<T> create(String msg) {
        return new SuccessResponse<>(msg, null);
    }

    public static <T> SuccessResponse<T> create(String msg, T data) {
        return new SuccessResponse<>(msg, data);
    }

    public SuccessResponse(String msg, T data) {
        super(msg);
        this.data = data;
    }

}
