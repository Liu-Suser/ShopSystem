package com.shiroyk.shopsystem.dto.response;

import com.shiroyk.shopsystem.constant.StatusCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class APIResponse<T> implements Serializable {
    private StatusCode code;
    private String msg;
    private T data;

    public APIResponse() {
    }

    public APIResponse(StatusCode code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public APIResponse(StatusCode code, String msg) {
        this(code, msg, null);
    }

    public static <T> APIResponse<T> ok() {
        return new APIResponse<>(StatusCode.Ok, "Ok");
    }

    public static <T> APIResponse<T> ok(String msg) {
        return new APIResponse<>(StatusCode.Ok, msg);
    }

    public static <T> APIResponse<T> ok(T data) {
        return new APIResponse<>(StatusCode.Ok, "Ok", data);
    }

    public static <T> APIResponse<T> ok(String msg, T data) {
        return new APIResponse<>(StatusCode.Ok, msg, data);
    }

    public static <T> APIResponse<T> badRequest(String msg) {
        return new APIResponse<>(StatusCode.BadRequest, msg);
    }

    public static <T> APIResponse<T> create(StatusCode code, String msg) {
        return new APIResponse<>(code, msg);
    }

    public static <T> APIResponse<T> create(StatusCode code, String msg, T data) {
        return new APIResponse<>(code, msg, data);
    }
}
