/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    void save(Comment comment);

    Optional<Comment> findById(Long id);

    Optional<Comment> findByOrderId(Long id);

    List<Comment> findCommentByProductId(Long id);
    
}
