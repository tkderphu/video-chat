package com.example.video_chat.api;

import com.example.video_chat.domain.modelviews.request.LoginRequest;
import com.example.video_chat.domain.modelviews.request.RegisterRequest;
import com.example.video_chat.repository.TokenRepository;
import com.example.video_chat.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;

    @BeforeEach
    void init() {
        loginRequest = new LoginRequest("quangphu@gmail.com", "quangphu");
        registerRequest = new RegisterRequest(
                loginRequest.getEmail(),
                loginRequest.getPassword(),
                "phu",
                "nguyen quang"
        );
    }

    @AfterEach
    void destroy() {
        tokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    ResultActions resLogin(String val) throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post(
                                "/api/v1/auth/login"
                        ).contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsBytes(this.loginRequest)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(val));
        return resultActions;
    }

    @Test
    void test_login_when_account_not_exists() throws Exception {
        resLogin("Username not found");
    }

    @Test
    void test_login_when_password_account_not_match() throws Exception {
        this.register_ok();
        this.loginRequest.setPassword("hello world");
        resLogin("Password which you type not match");
    }

    @Test
    void test_login_when_account_match() throws Exception {
        this.register_ok();
        resLogin("User login successfully");
    }

    @Test
    void register_ok() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(
                                "/api/v1/auth/register"
                        ).contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsBytes(this.registerRequest)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("User register account successfully"));
    }
}
