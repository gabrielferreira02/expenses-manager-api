package com.gabrielferreira02.expensesManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielferreira02.expensesManager.dto.LoginRequest;
import com.gabrielferreira02.expensesManager.dto.LoginResponse;
import com.gabrielferreira02.expensesManager.entity.UserEntity;
import com.gabrielferreira02.expensesManager.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void login() throws Exception {
        LoginRequest request = new LoginRequest("user", "1234");

        ResponseEntity<LoginResponse> response = ResponseEntity.ok(new LoginResponse("token"));

        when(authService.login(anyString(), anyString())).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"user\",\"password\":\"1234\"}")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("token"));

    }

    @Test
    void loginWithError() throws Exception {
        LoginRequest request = new LoginRequest("user1", "1234");
        String body = objectMapper.writeValueAsString(request);

        ResponseEntity<LoginResponse> response = ResponseEntity.ok(new LoginResponse("token"));

        when(authService.login(anyString(), anyString())).thenThrow(BadCredentialsException.class);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
        .andExpect(status().isForbidden());
    }

    @Test
    void register() throws Exception {
        LoginRequest request = new LoginRequest("user", "1234");

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setPassword(request.getPassword());
        user.setUsername(request.getUsername());

        String body = objectMapper.writeValueAsString(request);

        ResponseEntity<UserEntity> response  = ResponseEntity.ok(user);

        when(authService.register(anyString(), anyString())).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(request.getUsername()))
                .andExpect(jsonPath("$.password").value(request.getPassword()));
    }

    @Test
    void registerWithError() throws Exception {
        LoginRequest request = new LoginRequest("", "1234");

        String body = objectMapper.writeValueAsString(request);

        when(authService.register(anyString(), anyString())).thenThrow(BadCredentialsException.class);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isBadRequest());
    }
}