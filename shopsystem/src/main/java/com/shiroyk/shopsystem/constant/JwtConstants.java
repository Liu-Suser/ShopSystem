/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.constant;

public class JwtConstants {
    public static final long EXPIRATION = 1000 * 60 * 60 * 24 * 3; // 3天
    public static final String SECRET = "ThisIsASecret";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTH_LOGIN_URL = "/user/login";
}
