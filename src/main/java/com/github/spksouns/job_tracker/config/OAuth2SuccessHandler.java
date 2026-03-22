package com.github.spksouns.job_tracker.config;

import com.github.spksouns.job_tracker.entity.User;
import com.github.spksouns.job_tracker.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class OAuth2SuccessHandler
        implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oauthUser =
                (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setPassword("OAUTH2_USER");
                    return userRepository.save(newUser);
                });

        String token = jwtUtil.generateToken(user.getEmail());

        response.setContentType("application/json");
        response.getWriter().write(
                "{\"token\":\"" + token + "\"," +
                        "\"email\":\"" + email + "\"," +
                        "\"name\":\"" + name + "\"}"
        );
        response.getWriter().flush();
    }
}