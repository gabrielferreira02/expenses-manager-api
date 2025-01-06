package com.gabrielferreira02.expensesManager.service;

import com.gabrielferreira02.expensesManager.dto.LoginResponse;
import com.gabrielferreira02.expensesManager.entity.UserEntity;
import com.gabrielferreira02.expensesManager.repository.UserRepository;
import com.gabrielferreira02.expensesManager.utils.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthService(JwtUtils jwtUtils, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication.getName());

        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    @Transactional
    public ResponseEntity<?> register(String username, String password) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        return ResponseEntity.ok(userRepository.save(user));
    }
}
