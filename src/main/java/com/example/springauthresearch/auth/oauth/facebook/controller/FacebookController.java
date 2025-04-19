package com.example.springauthresearch.auth.oauth.facebook.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/oauth/facebook")
public class FacebookController {

    @GetMapping("/login")
    public String loginPage() {
        return "facebook-login"; // your HTML login page
    }

    @GetMapping("/welcome")
    public String welcome(@AuthenticationPrincipal OAuth2User user, Model model) {
        model.addAttribute("username", user.getAttribute("name"));
        return "facebook-welcome"; // your HTML welcome page
    }
}

