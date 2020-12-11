/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.exception;

public class NotFoundResourceException extends RuntimeException {

    public NotFoundResourceException() {
    }

    public NotFoundResourceException(String message) {
        super(message);
    }
}
