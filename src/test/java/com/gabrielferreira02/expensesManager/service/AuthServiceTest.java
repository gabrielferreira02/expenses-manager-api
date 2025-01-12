package com.gabrielferreira02.expensesManager.service;

import com.gabrielferreira02.expensesManager.dto.LoginResponse;
import com.gabrielferreira02.expensesManager.entity.UserEntity;
import com.gabrielferreira02.expensesManager.repository.UserRepository;
import com.gabrielferreira02.expensesManager.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthService authService;

    @Test
    public void testLoginWithValidCredentials() {
        String username = "user";
        String password = "1234";

        Authentication mockAuthentication = mock(Authentication.class);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        )).thenReturn(mockAuthentication);

        when(mockAuthentication.getName()).thenReturn(username);
        String jwt = "mockJwtToken";
        when(jwtUtils.generateToken(any(String.class))).thenReturn(jwt);

        ResponseEntity<?> response = authService.login(username, password);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jwt, ((LoginResponse) response.getBody()).token());
    }

    @Test
    public void testLoginWithBadCredentials() {
        String username = "user";
        String password = "1234";

        Authentication mockAuthentication = mock(Authentication.class);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        )).thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class, () -> {
            authService.login(username, password);
        });
    }

    @Test
    public void testRegisterWithValidParameters() {
        String username = "user";
        String password = "1234";

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        when(this.userRepository.save(any(UserEntity.class))).thenReturn(user);

        ResponseEntity<?> response = authService.register(username, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(username, ((UserEntity) response.getBody()).getUsername());
    }

    @Test
    public void testRegisterWithInvalidUsername() {
        String username = "user";
        String password = "1234";

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        when(this.userRepository.save(any(UserEntity.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> {
            authService.register(username, password);
        });
    }

}