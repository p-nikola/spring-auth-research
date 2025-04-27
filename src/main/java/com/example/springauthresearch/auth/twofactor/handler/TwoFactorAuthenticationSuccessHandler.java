package com.example.springauthresearch.auth.twofactor.handler;

import com.example.springauthresearch.auth.twofactor.service.TwoFactorService;
import com.example.springauthresearch.common.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class TwoFactorAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final TwoFactorService twoFactorService;


    public TwoFactorAuthenticationSuccessHandler(TwoFactorService twoFactorService) {
        this.twoFactorService = twoFactorService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req,
                                        HttpServletResponse res,
                                        Authentication auth) throws IOException {
        User user = (User) auth.getPrincipal();

        if (user.isTwoFactorEnabled()) {
            // kick them into pre‑2FA
            HttpSession session = req.getSession();
            session.setAttribute("pre2faUser", user);
            SecurityContextHolder.clearContext();
            res.sendRedirect("/auth/basic/2fa/verify");
        } else {
            // normal login
            res.sendRedirect("/auth/basic/home");
        }
    }
}