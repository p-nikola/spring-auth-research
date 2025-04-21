package com.example.springauthresearch.auth.passwordless.config;

import com.example.springauthresearch.auth.passwordless.service.PasswordlessUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.ott.JdbcOneTimeTokenService;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationProvider;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.ui.DefaultResourcesFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;

@Configuration
@Order(1)
public class PasswordlessSecurityConfig {

    private final JdbcTemplate jdbcTemplate;
    private final OttSuccessHandler ottSuccessHandler;
    private final PasswordlessUserDetailsService passwordlessUds;

    public PasswordlessSecurityConfig(
            JdbcTemplate jdbcTemplate, OttSuccessHandler ottSuccessHandler,
            @Qualifier("passwordlessUserDetailsService")
            PasswordlessUserDetailsService passwordlessUds
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.ottSuccessHandler = ottSuccessHandler;
        this.passwordlessUds = passwordlessUds;
    }

    @Bean
    public OneTimeTokenService tokenService() {
        return new JdbcOneTimeTokenService(jdbcTemplate);
    }


    @Bean
    SecurityFilterChain passwordlessChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(
                        "/auth/passwordless/**",
                        "/auth/passwordless/generate",
                        "/auth/passwordless/verify",
                        "/default-ui.css",  // for the built‑in CSS if you’re using DefaultResourcesFilter
                        "/error"
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/passwordless/login",
                                "/auth/passwordless/check-email",
                                "/auth/passwordless/generate",
                                "/auth/passwordless/verify",
                                "/default-ui.css",
                                "/error"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // 1) register the OTP auth provider that knows about your UDS
                .authenticationProvider(
                        new OneTimeTokenAuthenticationProvider(tokenService(), passwordlessUds)
                )

                // 2) wire in the CSS filter if you want the default UI.css
                .addFilterBefore(DefaultResourcesFilter.css(),
                        SecurityContextHolderFilter.class)

                // 3) your custom login page
                .formLogin(form -> form
                        .loginPage("/auth/passwordless/login")
                        .permitAll()
                )

                // 4) enable the OTT magic‑link DSL
                .oneTimeTokenLogin(ott -> ott
                        .tokenGeneratingUrl("/auth/passwordless/generate")
                        .loginProcessingUrl("/auth/passwordless/verify")
                        .defaultSubmitPageUrl("/auth/passwordless/verify")

                        // you’ve already registered the provider above, no userDetailsService() here
                        .tokenService(tokenService())
                        .tokenGenerationSuccessHandler(ottSuccessHandler)

                        // give the lambda explicit types so res.sendRedirect(...) compiles
                        .authenticationSuccessHandler((
                                HttpServletRequest req,
                                HttpServletResponse res,
                                Authentication auth
                        ) -> res.sendRedirect("/auth/passwordless/welcome"))
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/passwordless/logout")
                        .logoutSuccessUrl("/auth/passwordless/login?logout") // redirect after logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                );

        return http.build();
    }
}

