package com.example.springauthresearch.auth.oauth.github.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(3)
public class GitHubSecurityConfig {

    @Bean
    public SecurityFilterChain githubSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher(
                        "/auth/oauth/github/**",
                        "/oauth2/authorization/github",
                        "/login/oauth2/code/github"
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/oauth/github/login").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/auth/oauth/github/login")
                        .defaultSuccessUrl("/auth/oauth/github/welcome", true)
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/oauth/github/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}

