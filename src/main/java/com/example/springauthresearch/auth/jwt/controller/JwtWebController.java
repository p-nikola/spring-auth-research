package com.example.springauthresearch.auth.jwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JwtWebController {

    @GetMapping("/auth/jwt/login")
    public String jwtLoginPage() {
        return "jwt-login"; // maps to jwt-login.html
    }

    @GetMapping("/auth/jwt/welcome")
    public String welcomePage() {
        return "welcome"; // maps to welcome.html
    }

    @GetMapping("/auth/jwt/test-page")
    public String testPage() {
        return "jwt-test"; // maps to jwt-test.html
    }
}
