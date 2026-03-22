package com.github.spksouns.job_tracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication
        .UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",    // register, login, logout
                                "/oauth2/**",      // Google login start
                                "/login/**",       // OAuth2 callback
                                "/swagger-ui/**",  // Swagger UI pages
                                "/v3/api-docs/**"  // Swagger JSON docs
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler((request, response, exception) -> {
                            response.setContentType("application/json");
                            response.setStatus(401);
                            response.getWriter().write(
                                    "{\"error\": \"Google login failed!\"}");
                            response.getWriter().flush();
                        })
                )
                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}