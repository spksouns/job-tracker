package com.github.spksouns.job_tracker.controller;

import com.github.spksouns.job_tracker.config.JwtUtil;
import com.github.spksouns.job_tracker.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.github.spksouns.job_tracker.entity.User;
import com.github.spksouns.job_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;  // ← add this!


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid User user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());

        if(user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found!");
        }

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            return ResponseEntity.badRequest().body("Invalid password!");
        }
        // Generate JWT token!
        String token = jwtUtil.generateToken(user.get().getEmail());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", user.get().getEmail(),
                "name", user.get().getName()
        ));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully!");
    }
}