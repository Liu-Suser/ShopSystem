package com.shiroyk.shopsystem.entity;

import com.shiroyk.shopsystem.dto.response.CommentDto;
import com.shiroyk.shopsystem.dto.response.UserDto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class Comment implements Serializable {

    private Long id;
    private Long userId;
    private Long productId;
    private Integer rate;
    private Image image;
    private String comment;
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime updateTime = LocalDateTime.now();

    public CommentDto toCommentDto(UserDto userDto) {
        return new CommentDto(id, userId, userDto, rate, comment);
    }

}
