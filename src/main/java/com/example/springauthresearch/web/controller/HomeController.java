package com.example.springauthresearch.web.controller;

import com.example.springauthresearch.service.AuthService;
import com.example.springauthresearch.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = {"/", "", "/home"})
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        model.addAttribute("name",username);
        return "home";
    }

    @GetMapping("/access_denied")
    public String getAccessDeniedPage(Model model) {
        model.addAttribute("bodyContent", "access-denied");
        return "access-denied";
    }

}
