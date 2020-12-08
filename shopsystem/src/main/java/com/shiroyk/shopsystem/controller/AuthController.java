/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.controller;

import com.shiroyk.shopsystem.entity.User;
import com.shiroyk.shopsystem.entity.getEntity.ResponseMsg;
import com.shiroyk.shopsystem.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseMsg> signUp(String username,
                                              String password,
                                              String nickname,
                                              String phone) {
        if (userService.findUserByUsername(username) != null) {
            return ResponseEntity.badRequest().body(new ResponseMsg(HttpStatus.BAD_REQUEST,"用户已存在！"));
        } else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            User user = new User();
            user.setUsername(username);
            user.setPassword(encoder.encode(password));
            user.setPhone(phone);
            user.setNickname(nickname);
            userService.save(user);
            return ResponseEntity.ok().body(new ResponseMsg(HttpStatus.OK,"注册成功！"));
        }
    }

    @GetMapping("/question")
    public ResponseEntity<ResponseMsg> getForgetQuestion(@RequestParam("username") String username) {
        //获取用户的安全问题
        Optional<String> question = userService.getAnswerByUsername(username);
        return question.map(s -> ResponseEntity.ok()
                .body(new ResponseMsg(HttpStatus.OK, s))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMsg(HttpStatus.NOT_FOUND, "用户不存在！")));
    }

    @PutMapping("/forget")
    public ResponseEntity<ResponseMsg> forgetPassword(String username,
                                                      String answer,
                                                      String newPassword) {
        //对比安全问题答案，相同则修改密码
        User user = userService.findUserByUsername(username);
        if (user.getAnswer().equals(answer)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(newPassword));
            userService.save(user);
            return ResponseEntity.ok()
                    .body(new ResponseMsg(HttpStatus.OK, "修改密码成功！"));
        } else {
            return ResponseEntity.badRequest()
                    .body(new ResponseMsg(HttpStatus.BAD_REQUEST, "修改密码失败！"));
        }
    }

}
