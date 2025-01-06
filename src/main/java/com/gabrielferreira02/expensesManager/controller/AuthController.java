package com.gabrielferreira02.expensesManager.controller;

import com.gabrielferreira02.expensesManager.dto.LoginRequest;
import com.gabrielferreira02.expensesManager.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PreAuthorize("permitAll()")
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @PreAuthorize("permitAll()")
    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.register(loginRequest.getUsername(), loginRequest.getPassword());
    }
}
