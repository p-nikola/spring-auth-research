package com.example.springauthresearch.auth.oauth.reddit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@Order(7)
public class RedditSecurityConfig {

    @Bean
    public SecurityFilterChain redditSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher(
                        "/auth/oauth/reddit/**",
                        "/oauth2/authorization/reddit",
                        "/login/oauth2/code/reddit"
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/oauth/reddit/login").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/auth/oauth/reddit/login")
                        .defaultSuccessUrl("/auth/oauth/reddit/welcome", true)
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/oauth/reddit/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}
