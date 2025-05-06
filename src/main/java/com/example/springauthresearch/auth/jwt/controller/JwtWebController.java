package com.example.springauthresearch.auth.jwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JwtWebController {

    @GetMapping("/auth/jwt/login")
    public String jwtLoginPage() {
        return "jwt/jwt-login";
    }

    @GetMapping("/auth/jwt/welcome")
    public String welcomePage() {
        return "jwt/welcome";
    }

    @GetMapping("/auth/jwt/test-page")
    public String testPage() {
        return "jwt/jwt-test";
    }
}
