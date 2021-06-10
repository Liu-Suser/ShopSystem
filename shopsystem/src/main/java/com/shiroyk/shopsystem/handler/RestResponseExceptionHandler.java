/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.handler;

import com.shiroyk.shopsystem.constant.StatusCode;
import com.shiroyk.shopsystem.dto.response.APIResponse;
import com.shiroyk.shopsystem.exception.BadRequestException;
import com.shiroyk.shopsystem.exception.NotFoundResourceException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AuthenticationException.class,
                        UsernameNotFoundException.class,
                        SignatureException.class,
                        MalformedJwtException.class,
                        ExpiredJwtException.class,
                        IllegalArgumentException.class,
                       })
    public ResponseEntity<Object> handleUnauthorized(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(APIResponse.create(StatusCode.UnAuthorized, ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleForbidden(AccessDeniedException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(APIResponse.create(StatusCode.Forbidden, ex.getMessage()));
    }

    @ExceptionHandler(NotFoundResourceException.class)
    public ResponseEntity<Object> handleNotFound(NotFoundResourceException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(APIResponse.create(StatusCode.NotFound, ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequest(BadRequestException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.create(StatusCode.BadRequest, ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.create(StatusCode.BadRequest,
                        ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.info(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(APIResponse.create(StatusCode.BadGateWay, "服务器出了小问题！"));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(APIResponse.create(StatusCode.NotFound, ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getMessage());
        return ResponseEntity.status(status)
                .body(APIResponse.create(StatusCode.fromCode(status.value()), ex.getMessage()));
    }
}
