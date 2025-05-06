package com.example.springauthresearch.auth.twofactor.controller;

import com.example.springauthresearch.auth.twofactor.service.TwoFactorService;
import com.example.springauthresearch.common.model.User;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth/basic/2fa")
public class TwoFactorController {

    private final TwoFactorService twoFactorService;

    public TwoFactorController(TwoFactorService twoFactorService) {
        this.twoFactorService = twoFactorService;
    }

    /** STEP 1: Show the QR‐code setup page */
    @GetMapping("/setup")
    public String showSetup(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        GoogleAuthenticatorKey key = twoFactorService.generateKey(username);
        model.addAttribute("qrUrl", GoogleAuthenticatorQRGenerator.getOtpAuthURL("MyApp", username, key));
        return "2fa/2fa-setup";
    }

    /** STEP 2: Confirm the first code & enable 2FA */
    @PostMapping("/setup")
    public String enable(@RequestParam int code) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (twoFactorService.verifyCode(username, code)) {
            twoFactorService.enableTwoFactor(username);
            return "redirect:/auth/basic/home";
        }
        return "redirect:/auth/basic/2fa/setup?error";
    }

    /** STEP 3: Show the TOTP prompt during login */
    @GetMapping("/verify")
    public String showVerify() {
        return "2fa/2fa-verify";
    }

    /** STEP 4: Verify TOTP on login */
    @PostMapping("/verify")
    public String verify(@RequestParam int code, HttpSession session) {
        User pending = (User) session.getAttribute("pre2faUser");
        if (pending != null && twoFactorService.verifyCode(pending.getUsername(), code)) {
            session.setAttribute("user", pending);
            session.removeAttribute("pre2faUser");
            return "redirect:/auth/basic/home";
        }
        return "redirect:/auth/basic/2fa/verify?error";
    }

    @PostMapping("/disable")
    public String disable2fa() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        twoFactorService.disableTwoFactor(username);
        return "redirect:/auth/basic/home?2faDisabled";
    }
}
