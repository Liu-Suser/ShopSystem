package com.shiroyk.shopsystem.service.impl;

import com.shiroyk.shopsystem.entity.Comment;
import com.shiroyk.shopsystem.mapper.CommentMapper;
import com.shiroyk.shopsystem.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public void save(Comment comment) {
        commentMapper.save(comment);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentMapper.findById(id);
    }

    @Override
    public Optional<Comment> findByOrderId(Long id) {
        return commentMapper.findByOrderId(id);
    }

    @Override
    public List<Comment> findCommentByProductId(Long id) {
        return commentMapper.findCommentsByProductIdId(id);
    }
}
