package com.shiroyk.shopsystem.dto.response;

import com.shiroyk.shopsystem.constant.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String nickname;
    private Integer userpoint;
    private String phone;
    private String question;
    private String answer;
    private UserRole role;

    public UserDto() {
    }

    public UserDto(Long id, String username, String nickname, Integer userpoint) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.userpoint = userpoint;
    }

    public UserDto(Long id, String username, String nickname, Integer userpoint, String phone) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.userpoint = userpoint;
        this.phone = phone;
    }
}
