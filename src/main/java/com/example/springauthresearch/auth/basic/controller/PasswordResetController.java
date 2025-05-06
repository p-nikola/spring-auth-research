package com.example.springauthresearch.auth.basic.controller;

import com.example.springauthresearch.auth.basic.model.PasswordResetToken;
import com.example.springauthresearch.auth.basic.repository.PasswordResetTokenRepository;
import com.example.springauthresearch.common.service.EmailService;
import com.example.springauthresearch.common.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/auth/basic/password")
public class PasswordResetController {

    private final PasswordResetTokenRepository tokenRepo;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.base-url}")
    private String baseUrl;

    public PasswordResetController(PasswordResetTokenRepository tokenRepo, UserService userService, PasswordEncoder passwordEncoder, @Qualifier("emailservicebasic") EmailService emailService) {
        this.tokenRepo = tokenRepo;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @GetMapping("/forgot")
    public String forgotPasswordForm() {
        return "basic/forgot-password";
    }

    @PostMapping("/forgot")
    public String handleForgot(@RequestParam String email) {
        // Check if user exists
        var user = userService.findByEmail(email).orElse(null);
        if (user == null) return "basic/email-not-found";

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(email);
        resetToken.setExpiration(LocalDateTime.now().plusMinutes(30));
        tokenRepo.save(resetToken);

        emailService.sendResetPasswordEmail(email, token);
        System.out.println("Password reset link: " + baseUrl + "/auth/basic/password/reset?token=" + token);
        return "basic/check-your-email";
    }

    @GetMapping("/reset")
    public String showResetForm(@RequestParam String token, org.springframework.ui.Model model) {
        model.addAttribute("token", token);
        return "basic/reset-password";
    }

    @PostMapping("/reset")
    public String handleReset(@RequestParam String token, @RequestParam String newPassword) {
        var tokenEntity = tokenRepo.findByToken(token).orElse(null);
        if (tokenEntity == null || tokenEntity.getExpiration().isBefore(LocalDateTime.now())) {
            return "basic/invalid-token";
        }

        var user = userService.findByEmail(tokenEntity.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(user);

        tokenRepo.delete(tokenEntity);

        return "basic/password-reset-success";
    }

}
