// com/example/auth/passwordless/controller/PasswordlessLoginController.java
package com.example.springauthresearch.auth.passwordless.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/passwordless")
public class PasswordlessLoginController {

    @GetMapping("/login")
    public String login() {
        return "passwordless/passwordless-login";
    }

    @GetMapping("/check-email")
    public String checkEmail() {
        return "passwordless/check-email";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "passwordless/welcome-passwordless";
    }
}
