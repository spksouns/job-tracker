package com.github.spksouns.job_tracker.controller;

import com.github.spksouns.job_tracker.config.JwtUtil;
import com.github.spksouns.job_tracker.dto.LoginRequest;
import com.github.spksouns.job_tracker.entity.User;
import com.github.spksouns.job_tracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)   // ✅ No Spring, pure Mockito — works with any version
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    // ─────────────────────────────────────────
    // 1. Register — success
    // ─────────────────────────────────────────
    @Test
    void register_shouldReturn200_whenNewUser() {
        // Arrange
        User user = new User();
        user.setEmail("praveen@example.com");
        user.setPassword("password123");
        user.setName("Praveen");

        when(userRepository.findByEmail("praveen@example.com"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPass");
        when(userRepository.save(any())).thenReturn(user);

        // Act
        ResponseEntity<?> response = authController.register(user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully!", response.getBody());
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(user);
    }

    // ─────────────────────────────────────────
    // 2. Register — duplicate email
    // ─────────────────────────────────────────
    @Test
    void register_shouldReturn400_whenEmailAlreadyExists() {
        // Arrange
        User user = new User();
        user.setEmail("praveen@example.com");
        user.setPassword("password123");

        when(userRepository.findByEmail("praveen@example.com"))
                .thenReturn(Optional.of(user));   // ← already exists!

        // Act
        ResponseEntity<?> response = authController.register(user);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already exists!", response.getBody());
        verify(userRepository, never()).save(any());  // must NOT save
    }

    // ─────────────────────────────────────────
    // 3. Login — success
    // ─────────────────────────────────────────
    @Test
    @SuppressWarnings("unchecked")
    void login_shouldReturnToken_whenValidCredentials() {
        // Arrange
        User user = new User();
        user.setEmail("praveen@example.com");
        user.setPassword("encodedPass");
        user.setName("Praveen");

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("praveen@example.com");
        loginRequest.setPassword("password123");

        when(userRepository.findByEmail("praveen@example.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPass"))
                .thenReturn(true);
        when(jwtUtil.generateToken("praveen@example.com"))
                .thenReturn("mocked-jwt-token");

        // Act
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("mocked-jwt-token", body.get("token"));
        assertEquals("praveen@example.com", body.get("email"));
        assertEquals("Praveen", body.get("name"));
    }

    // ─────────────────────────────────────────
    // 4. Login — user not found
    // ─────────────────────────────────────────
    @Test
    void login_shouldReturn400_whenUserNotFound() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("unknown@example.com");
        loginRequest.setPassword("password123");

        when(userRepository.findByEmail("unknown@example.com"))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User not found!", response.getBody());
    }

    // ─────────────────────────────────────────
    // 5. Login — wrong password
    // ─────────────────────────────────────────
    @Test
    void login_shouldReturn400_whenWrongPassword() {
        // Arrange
        User user = new User();
        user.setEmail("praveen@example.com");
        user.setPassword("encodedPass");

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("praveen@example.com");
        loginRequest.setPassword("wrongpassword");

        when(userRepository.findByEmail("praveen@example.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPass"))
                .thenReturn(false);

        // Act
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid password!", response.getBody());
        verify(jwtUtil, never()).generateToken(any()); // must NOT generate token
    }
}