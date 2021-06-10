/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    void save(User user);

    void delete(long uid);

    Optional<String> getAnswerByUsername(String username);

    User findUserByUsername(String username);

    List<User> searchUserByUsername(String username);

    Optional<User> findById(Long id);

    User get(Long id);

    Optional<User> getCurrentUser();

    String getCurrentUserName();

    List<User> findAll();

    List<User> findUserByIdList(List<Long> idList);
}
