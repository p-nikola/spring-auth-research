package com.example.springauthresearch.auth.passwordless.config;

import com.example.springauthresearch.auth.passwordless.service.EmailServicePasswordlessImpl;
import com.example.springauthresearch.auth.passwordless.service.PasswordlessUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OttSuccessHandler implements OneTimeTokenGenerationSuccessHandler {
    private final PasswordlessUserService userService;
    private final EmailServicePasswordlessImpl emailServicePasswordless;

    // after you generate the token, spring will redirect to this page
    private final OneTimeTokenGenerationSuccessHandler redirect
            = new RedirectOneTimeTokenGenerationSuccessHandler(
            "/auth/passwordless/check-email"
    );

    public OttSuccessHandler(PasswordlessUserService userService, @Qualifier("passwordlessemail") EmailServicePasswordlessImpl emailServicePasswordless) {
        this.userService = userService;
        this.emailServicePasswordless = emailServicePasswordless;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       OneTimeToken token)
            throws IOException, ServletException {

        // 1) persist user if new
        userService.findOrSaveUser(token.getUsername());

        // 2) build the magic-link
        String magicLink = UriComponentsBuilder
                .fromHttpUrl(request.getRequestURL().toString())
                .replacePath(request.getContextPath())
                .path("/auth/passwordless/verify")
                .queryParam("token", token.getTokenValue())
                .build()
                .toUriString();

        // 3) “send” it
        System.out.println("magic link: " + magicLink);

        emailServicePasswordless.sendResetPasswordEmail(token.getUsername(),magicLink);

        // 4) continue with default redirect
        redirect.handle(request, response, token);
    }
}
