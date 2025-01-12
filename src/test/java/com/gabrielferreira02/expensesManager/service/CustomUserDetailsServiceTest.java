package com.gabrielferreira02.expensesManager.service;

import com.gabrielferreira02.expensesManager.entity.UserEntity;
import com.gabrielferreira02.expensesManager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void testLoadUserByUsername() {
        String username  = "user";

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("1234");
        user.setId(UUID.randomUUID());

        when(this.userRepository.findByUsername(any(String.class))).thenReturn(user);

        UserDetails response = customUserDetailsService.loadUserByUsername(username);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        assertNotNull(response);
        assertEquals(username, response.getUsername());
        assertTrue(authorities.containsAll(response.getAuthorities()));
    }

    @Test
    void testLoadUserByUsernameWithInvalidUser() {
        String username  = "user";

        when(this.userRepository.findByUsername(any(String.class))).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(username);
        });
    }


}