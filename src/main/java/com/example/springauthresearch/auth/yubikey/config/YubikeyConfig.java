package com.example.springauthresearch.auth.yubikey.config;

import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import com.example.springauthresearch.auth.yubikey.repository.CustomCredentialRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Set;

@Configuration
public class YubikeyConfig {


    @Bean
    public SecurityFilterChain yubikeySecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/auth/yubikey/**")

                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authz ->
                        authz.anyRequest().permitAll()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.ALWAYS
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
