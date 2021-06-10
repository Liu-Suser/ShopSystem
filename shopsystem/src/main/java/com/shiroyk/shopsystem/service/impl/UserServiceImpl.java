package com.shiroyk.shopsystem.service.impl;

import com.shiroyk.shopsystem.entity.JwtUser;
import com.shiroyk.shopsystem.entity.User;
import com.shiroyk.shopsystem.mapper.UserMapper;
import com.shiroyk.shopsystem.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void save(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.save(user);
    }

    @Override
    public void delete(long uid) {
        userMapper.delete(uid);
    }

    @Override
    public Optional<String> getAnswerByUsername(String username) {
        return userMapper.getAnswerByUsername(username);
    }

    @Override
    public User findUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    @Override
    public List<User> searchUserByUsername(String username) {
        return userMapper.findUsersByUsernameContains(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public User get(Long id) {
        return userMapper.findById(id).orElse(null);
    }

    @Override
    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (authentication != null && authentication.getPrincipal() != null) {
            user = findUserByUsername((String) authentication.getPrincipal());
        }
        return Optional.ofNullable(user);
    }

    @Override
    public String getCurrentUserName() {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findUserByIdList(List<Long> idList) {
        return userMapper.findUserByIdList(idList);
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return new JwtUser(userMapper.findUserByUsername(s));
    }
}
