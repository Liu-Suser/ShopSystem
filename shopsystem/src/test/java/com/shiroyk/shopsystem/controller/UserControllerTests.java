package com.shiroyk.shopsystem.controller;

import com.shiroyk.shopsystem.constant.JwtConstants;
import com.shiroyk.shopsystem.utils.JwtTokenUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class UserControllerTests {

    private String token;

    @Autowired
    private WebTestClient webTestClient;

    private String getToken() {
        if (token == null) {
            token = JwtTokenUtils.createToken("test");
        }
        return JwtConstants.TOKEN_PREFIX + " " + token;
    }

    @Test
    public void testLogin() {
        webTestClient.post().uri("/user/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("username=test&password=test")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").isNotEmpty();
    }

    @Test
    public void testSignup() {
        webTestClient.post().uri("/user/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("username=test&password=test")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .consumeWith(result -> assertThat(result.getResponseBody()).contains("用户已存在！"));
    }

    @Test
    public void testGetUserInfo() {
        webTestClient.get().uri("/user/1")
                .header("Authorization", getToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("admin");
    }

    @Test
    public void testGetInfo() {
        webTestClient.get().uri("/user/info")
                .header("Authorization", getToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("test");
    }

    @Test
    public void testUpdateInfo() {
        webTestClient.put().uri("/user/info")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("nickname=test&phone=111")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testUpdatePassword() {
        webTestClient.put().uri("/user/password")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("newPassword=test")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testGetForgetQuestion() {
        webTestClient.get().uri("/user/question?username=admin")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testForgetPassword() {
        webTestClient.put().uri("/user/forget")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("username=test&answer=test&newPassword=test")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testGetUserOrder() {
        webTestClient.get().uri("/user/order")
                .header("Authorization", getToken())
                .exchange()
                .expectStatus().isOk();
    }
}
