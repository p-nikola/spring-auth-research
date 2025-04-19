package com.example.springauthresearch.auth.oauth.discord.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(6)
public class DiscordSecurityConfig {

    @Bean
    public SecurityFilterChain discordSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher(
                        "/auth/oauth/discord/**",
                        "/oauth2/authorization/discord",
                        "/login/oauth2/code/discord"
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/oauth/discord/login").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/auth/oauth/discord/login")
                        .defaultSuccessUrl("/auth/oauth/discord/welcome", true)
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/oauth/discord/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}
