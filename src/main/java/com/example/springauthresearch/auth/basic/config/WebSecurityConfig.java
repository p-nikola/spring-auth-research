package com.example.springauthresearch.auth.basic.config;

import com.example.springauthresearch.auth.twofactor.handler.TwoFactorAuthenticationSuccessHandler;
import com.example.springauthresearch.auth.twofactor.service.TwoFactorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final CustomUsernamePasswordAuthenticationProvider authProvider;
    private final TwoFactorService twoFactorService;

    public WebSecurityConfig(CustomUsernamePasswordAuthenticationProvider authProvider, TwoFactorService twoFactorService) {
        this.authProvider = authProvider;
        this.twoFactorService = twoFactorService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher("/auth/basic/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/basic/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/auth/basic/login")
                        .loginProcessingUrl("/auth/basic/login")
                        .successHandler(twoFactorSuccessHandler(twoFactorService))
                        .failureUrl("/auth/basic/login?error=BadCredentials")
                        .permitAll()

                )
                .logout((logout) -> logout
                        .logoutUrl("/auth/basic/logout")
                        .logoutSuccessUrl("/auth/basic/login")
                )
                .exceptionHandling((ex) -> ex
                        .accessDeniedPage("/auth/basic/access_denied")
                );

        return http.build();

    }

    @Bean
    public AuthenticationSuccessHandler twoFactorSuccessHandler(TwoFactorService twoFactorService) {
        return new TwoFactorAuthenticationSuccessHandler(twoFactorService);
    }


    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }
}

