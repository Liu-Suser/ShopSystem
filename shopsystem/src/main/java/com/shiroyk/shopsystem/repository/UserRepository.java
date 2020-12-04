/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.repository;

import com.shiroyk.shopsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user.question FROM User user WHERE user.username = :username")
    Optional<String> getAnswerByUsername(@Param("username") String username);

    List<User> findUsersByUsernameContains(String username);

    User findUserByUsername(String username);

}
