package com.shiroyk.shopsystem.mapper;

import com.shiroyk.shopsystem.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface CommentMapper {
    int save(Comment comment);

    Optional<Comment> findById(Long id);

    Optional<Comment> findByOrderId(Long id);

    List<Comment> findCommentsByProductIdId(long id);
}
