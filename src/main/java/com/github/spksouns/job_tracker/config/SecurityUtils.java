package com.github.spksouns.job_tracker.config;

import com.github.spksouns.job_tracker.entity.User;
import com.github.spksouns.job_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();

        Object principal = auth.getPrincipal();

        String email;
        if (principal instanceof User) {
            email = ((User) principal).getEmail();
        } else {
            email = principal.toString();
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found!"));
    }
}