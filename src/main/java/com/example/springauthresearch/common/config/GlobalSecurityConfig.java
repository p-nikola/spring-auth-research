package com.example.springauthresearch.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
public class GlobalSecurityConfig {

    @Bean
    public SecurityFilterChain globalFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**") // catch everything else
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().denyAll() // prevent access to anything else unless configured
                )
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}