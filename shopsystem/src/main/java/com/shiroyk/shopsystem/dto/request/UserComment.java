package com.shiroyk.shopsystem.dto.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

@Data
public class UserComment {
    @Max(message = "评分应在0-9之间", value = 9)
    private Integer rate;
    @Size(message = "评价字数不能小于5或大于100", min = 5, max = 100)
    private String comment;
}
