package com.example.springauthresearch.auth.basic.controller;

import com.example.springauthresearch.common.model.User;
import com.example.springauthresearch.common.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = {"/auth/basic/home"})
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        User current = (User) userService.loadUserByUsername(username);

        model.addAttribute("name",username);
        model.addAttribute("user",current);
        return "home";
    }

    @GetMapping("/access_denied")
    public String getAccessDeniedPage(Model model) {
        model.addAttribute("bodyContent", "access-denied");
        return "access-denied";
    }

}
