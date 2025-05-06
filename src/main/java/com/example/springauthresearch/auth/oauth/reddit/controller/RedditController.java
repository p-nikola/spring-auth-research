package com.example.springauthresearch.auth.oauth.reddit.controller;


import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/auth/oauth/reddit")
public class RedditController {

    @GetMapping("/login")
    public String login() {
        return "oauth/reddit/reddit-login"; // your HTML file
    }

    @GetMapping("/welcome")
    public String welcome(@AuthenticationPrincipal OAuth2User principal, Model model) {
        model.addAttribute("username", principal.getAttribute("name"));
        return "oauth/reddit/reddit-welcome"; // your HTML file
    }
}
