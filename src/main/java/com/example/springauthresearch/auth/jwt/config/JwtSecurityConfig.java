package com.example.springauthresearch.auth.jwt.config;

import com.example.springauthresearch.auth.jwt.filter.JwtAuthFilter;
import com.example.springauthresearch.common.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(1)
@EnableMethodSecurity
public class JwtSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public JwtSecurityConfig(JwtAuthFilter jwtAuthFilter, UserService userService, PasswordEncoder passwordEncoder) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/auth/jwt/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/jwt/login").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(jwtAuthProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider jwtAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    //@Bean(name = "jwtAuthManager")
    public AuthenticationManager jwtAuthManager(HttpSecurity http, AuthenticationProvider jwtAuthProvider) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(jwtAuthProvider)
                .build();
    }

}
