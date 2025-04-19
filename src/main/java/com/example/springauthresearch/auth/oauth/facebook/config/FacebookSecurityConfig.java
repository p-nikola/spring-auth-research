package com.example.springauthresearch.auth.oauth.facebook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@Order(7)
public class FacebookSecurityConfig {

    @Bean
    public SecurityFilterChain facebookSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/auth/oauth/facebook/**", "/login/oauth2/code/facebook", "/oauth2/authorization/facebook")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/oauth/facebook/login").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/auth/oauth/facebook/login")
                        .defaultSuccessUrl("/auth/oauth/facebook/welcome", true)
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/oauth/facebook/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}
