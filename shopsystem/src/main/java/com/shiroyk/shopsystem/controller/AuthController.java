/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.controller;

import com.shiroyk.shopsystem.entity.JwtUser;
import com.shiroyk.shopsystem.entity.User;
import com.shiroyk.shopsystem.dto.response.LoginResponse;
import com.shiroyk.shopsystem.dto.response.SuccessResponse;
import com.shiroyk.shopsystem.exception.BadRequestException;
import com.shiroyk.shopsystem.exception.NotFoundResourceException;
import com.shiroyk.shopsystem.service.UserService;
import com.shiroyk.shopsystem.utils.JwtTokenUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public SuccessResponse<Object> login(String username,
                                         String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JwtTokenUtils.createToken(authentication.getName());
        JwtUser user = (JwtUser) authentication.getPrincipal();

        return SuccessResponse.create("Success", new LoginResponse(user, token));
    }

    @PostMapping("/signup")
    public SuccessResponse<Object> signUp(String username,
                                          String password,
                                          String nickname,
                                          String phone) {
        if (userService.findUserByUsername(username) != null) {
            throw new NotFoundResourceException( "用户已存在！");
        } else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            User user = new User();
            user.setUsername(username);
            user.setPassword(encoder.encode(password));
            user.setPhone(phone);
            user.setNickname(nickname);
            userService.save(user);
            return SuccessResponse.create("注册成功！");
        }
    }

    @GetMapping("/question")
    public SuccessResponse<String> getForgetQuestion(@RequestParam("username") String username) {
        //获取用户的安全问题
        Optional<String> question = userService.getAnswerByUsername(username);
        return question
                .map(s -> SuccessResponse.create("Success", s))
                .orElseThrow(() -> new NotFoundResourceException("用户不存在！"));
    }

    @PutMapping("/forget")
    public SuccessResponse<Object> forgetPassword(String username,
                                                  String answer,
                                                  String newPassword) {
        //对比安全问题答案，相同则修改密码
        User user = userService.findUserByUsername(username);
        if (user.getAnswer().equals(answer)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(newPassword));
            userService.save(user);
            return SuccessResponse.create("修改密码成功！");
        } else {
            throw new BadRequestException("修改密码失败！");
        }
    }

}
