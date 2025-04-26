package com.example.springauthresearch.auth.basic.controller;

import com.example.springauthresearch.auth.basic.service.AuthService;
import com.example.springauthresearch.common.model.User;
import com.example.springauthresearch.common.model.enumerations.Role;
import com.example.springauthresearch.common.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth/basic")
public class BasicAuthController {

    private final AuthService authService;
    private final UserService userService;

    public BasicAuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }


    @GetMapping("/login")
    public String getLoginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        return "login";
    }

    //@PostMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            User user = authService.login(username, password);

            if (!user.isTwoFactorEnabled()) {
                // no 2FA -> full login
                request.getSession().setAttribute("user", user);
                return "redirect:/home";
            }

            // 2FA is enabled: hold them in a “pending” state
            request.getSession().setAttribute("pre2faUser", user);
            return "redirect:/auth/basic/2fa/verify";

        } catch (RuntimeException ex) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", ex.getMessage());
            return "login";
        }
    }



    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/auth/basic/login";
    }


    @GetMapping("/register")
    public String getRegisterPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String repeatedPassword,
                           @RequestParam String email
    ) {
        try {
            this.userService.register(username, password, repeatedPassword,email, Role.ROLE_USER);
            return "redirect:/auth/basic/login";
        } catch (RuntimeException ex) {
            return "redirect:/auth/basic/register?error=" + ex.getMessage();
        }
    }
}
