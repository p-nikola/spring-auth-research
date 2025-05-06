package com.example.springauthresearch.auth.oauth.discord.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/oauth/discord")
public class DiscordController {

    @GetMapping("/login")
    public String login() {
        return "oauth/discord/discord-login"; // Create a simple login page
    }

    @GetMapping("/welcome")
    public String welcome(@AuthenticationPrincipal OAuth2User principal, Model model) {
        model.addAttribute("username", principal.getAttribute("username")); // From Discord
        return "oauth/discord/discord-welcome";
    }
}
