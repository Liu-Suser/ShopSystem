/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.dto.response;

public class CommonResponse<T> extends BaseResponse {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> CommonResponse<T> create(T data) {
        return new CommonResponse<>("Success", data);
    }

    public static <T> CommonResponse<T> create(String msg) {
        return new CommonResponse<>(msg, null);
    }

    public static <T> CommonResponse<T> create(String msg, T data) {
        return new CommonResponse<>(msg, data);
    }

    public CommonResponse(String msg, T data) {
        super(msg);
        this.data = data;
    }

}
