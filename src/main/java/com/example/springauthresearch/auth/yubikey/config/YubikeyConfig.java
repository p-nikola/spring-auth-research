package com.example.springauthresearch.auth.yubikey.config;

import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import com.example.springauthresearch.auth.yubikey.repository.CustomCredentialRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.Set;

@Configuration
public class YubikeyConfig {


    @Bean
    @Order(1)
    public SecurityFilterChain yubikeySecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(
                        "/auth/yubikey/**"
                )

                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authz ->
                        authz
                                .requestMatchers(
                                        "/auth/yubikey/register/options",
                                        "/auth/yubikey/register",
                                        "/auth/yubikey/login/options",
                                        "/auth/yubikey/login",
                                        "/auth/yubikey/logout"
                                ).permitAll()
                                .requestMatchers("/auth/yubikey/welcome").authenticated()
                                .anyRequest().permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/yubikey/logout") // <-- set custom logout URL
                        .logoutSuccessUrl("/auth/yubikey/login") // <-- redirect after logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )

                .securityContext(secCtx -> secCtx
                        .securityContextRepository(new HttpSessionSecurityContextRepository())
                        .requireExplicitSave(true)
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.IF_REQUIRED
                        )
                );

        return http.build();
    }



    @Bean
    public RelyingParty relyingParty(CustomCredentialRepository credRepo) {
        return RelyingParty.builder()
                .identity(RelyingPartyIdentity.builder()
                        .id("localhost")
                        .name("MyApp")
                        .build())
                .credentialRepository(credRepo)
                .origins(Set.of("http://localhost:9090"))
                .build();
    }
}
