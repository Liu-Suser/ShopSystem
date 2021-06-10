package com.shiroyk.shopsystem.mapper;

import com.shiroyk.shopsystem.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface UserMapper {

    int save(User user);

    int delete(Long id);

    Optional<User> findById(Long id);

    Optional<String> getAnswerByUsername(String username);

    List<User> findUserByIdList(List<Long> idList);

    List<User> findUsersByUsernameContains(String username);

    User findUserByUsername(String username);
}
