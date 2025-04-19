package com.example.springauthresearch.auth.oauth.google.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(4)
public class GoogleSecurityConfig {

    @Bean
    public SecurityFilterChain googleSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher(
                        "/auth/oauth/google/**",
                        "/oauth2/authorization/google",
                        "/login/oauth2/code/google"
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/oauth/google/login").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/auth/oauth/google/login")
                        .defaultSuccessUrl("/auth/oauth/google/welcome", true)
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/oauth/google/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}

