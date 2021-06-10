package com.shiroyk.shopsystem.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusCode {
    Ok(200),
    BadRequest(400),
    UnAuthorized(401),
    Forbidden(403),
    NotFound(404),
    MethodNotAllowed(405),
    InternalError(500),
    NotImplemented(501),
    BadGateWay(502),
    ServiceUnavailable(503),
    GatewayTimeout(504);

    @JsonValue
    private final Integer code;

    StatusCode(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    @JsonCreator
    public static StatusCode fromCode(int code) {
        for (StatusCode c : StatusCode.values()) {
            if (c.code == code) {
                return c;
            }
        }
        return Ok;
    }
}
