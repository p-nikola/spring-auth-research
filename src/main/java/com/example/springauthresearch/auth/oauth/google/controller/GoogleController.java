package com.example.springauthresearch.auth.oauth.google.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/oauth/google")
public class GoogleController {

    @GetMapping("/login")
    public String loginPage() {
        return "google-login"; // maps to google-login.html
    }

    @GetMapping("/welcome")
    public String welcome(@AuthenticationPrincipal OAuth2User user, Model model) {
        model.addAttribute("username", user.getAttribute("name")); // or "email"
        return "google-welcome"; // maps to google-welcome.html
    }
}
