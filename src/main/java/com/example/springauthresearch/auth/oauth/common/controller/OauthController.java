package com.example.springauthresearch.auth.oauth.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/oauth")
public class OauthController {


    @GetMapping("/login")
    public String oauthMainLoginPage() {
        return "oauth-login";

    }


}
