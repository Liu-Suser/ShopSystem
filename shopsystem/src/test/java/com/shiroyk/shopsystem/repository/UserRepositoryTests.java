package com.shiroyk.shopsystem.repository;

import com.shiroyk.shopsystem.entity.User;
import com.shiroyk.shopsystem.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUsername("test99");
        this.entityManager.persist(user);
        User newUser = this.userMapper.findUserByUsername("test99");
        assertThat(newUser.getId()).isNotNull();
    }
}
