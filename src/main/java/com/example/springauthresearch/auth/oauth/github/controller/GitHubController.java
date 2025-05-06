package com.example.springauthresearch.auth.oauth.github.controller;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/oauth/github")
public class GitHubController {

    @GetMapping("/login")
    public String oauthLoginPage() {
        return "oauth/github/github-login"; // Thymeleaf template name
    }


    @GetMapping("/welcome")
    public String welcome(@AuthenticationPrincipal OAuth2User principal, Model model) {
        model.addAttribute("username", principal.getAttribute("login")); 
        return "oauth/github/github-welcome"; 
    }
}
