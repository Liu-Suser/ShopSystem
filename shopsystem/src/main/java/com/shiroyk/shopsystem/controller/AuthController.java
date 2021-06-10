/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.controller;

import com.shiroyk.shopsystem.dto.response.TokenDto;
import com.shiroyk.shopsystem.entity.JwtUser;
import com.shiroyk.shopsystem.entity.User;
import com.shiroyk.shopsystem.dto.response.APIResponse;
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
    
    /** 
    * @Description: 用户登录
    * @Param: [username, password] 
    * @return: 成功或失败消息
    */ 
    @PostMapping("/login")
    public APIResponse<Object> login(String username,
                                     String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JwtTokenUtils.createToken(authentication.getName());
        JwtUser user = (JwtUser) authentication.getPrincipal();

        return APIResponse.ok(new TokenDto(user.getUsername(), token));
    }
    
    /** 
    * @Description: 用户注册
    * @Param: [username, password, nickname, phone] 
    * @return: 成功或失败消息
    */ 
    @PostMapping("/signup")
    public APIResponse<Object> signUp(String username,
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
            return APIResponse.ok("注册成功！");
        }
    }

    /**
    * @Description: 获取用户的安全问题
    * @Param: [username]
    * @return: 安全问题
    */
    @GetMapping("/question")
    public APIResponse<?> getForgetQuestion(@RequestParam("username") String username) {
        //获取用户的安全问题
        Optional<String> question = userService.getAnswerByUsername(username);
        return question
                .map(APIResponse::ok)
                .orElseThrow(() -> new NotFoundResourceException("用户不存在！"));
    }
    
    /** 
    * @Description: 重置密码
    * @Param: [username, answer, newPassword] 
    * @return: 成功或失败消息
    */ 
    @PutMapping("/forget")
    public APIResponse<Object> forgetPassword(String username,
                                                  String answer,
                                                  String newPassword) {
        //对比安全问题答案，相同则修改密码
        User user = userService.findUserByUsername(username);
        if (user.getAnswer().equals(answer)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(newPassword));
            userService.save(user);
            return APIResponse.ok("修改密码成功！");
        } else {
            throw new BadRequestException("修改密码失败！");
        }
    }

}
