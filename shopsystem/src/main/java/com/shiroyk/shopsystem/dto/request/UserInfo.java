package com.shiroyk.shopsystem.dto.request;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UserInfo {
    @Size(message = "昵称长度不能小于2或大于10", min = 2, max = 10)
    private String nickname;
    @Size(message = "手机号长度不能小于2或大于11", min = 3, max = 11)
    private String phone;
    @Size(message = "安全问题长度不能小于4或大于100", min = 4, max = 100)
    private String question;
    @Size(message = "安全答案长度不能小于4或大于100", min = 4, max = 100)
    private String answer;
}
