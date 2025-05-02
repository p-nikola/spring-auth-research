package com.example.springauthresearch.auth.yubikey.web;

import com.example.springauthresearch.auth.yubikey.service.YubikeyService;
import com.yubico.webauthn.AssertionResult;
import com.yubico.webauthn.exception.RegistrationFailedException;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.data.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.yubico.webauthn.data.PublicKeyCredential;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/auth/yubikey")
public class YubikeyController {

    private final YubikeyService svc;
    private final UserDetailsService userDetailsService;

    public YubikeyController(
            YubikeyService svc,
            UserDetailsService userDetailsService
    ) {
        this.svc = svc;
        this.userDetailsService = userDetailsService;
    }


    @GetMapping("/register")
    public String registerPage() {
        return "yubikey/register";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "yubikey/login";
    }


    /** 1. Start registration: returns the PublicKeyCredentialCreationOptions JSON */
    @PostMapping("/register/options")
    @ResponseBody
    public PublicKeyCredentialCreationOptions registrationOptions(
            @RequestBody Map<String, String> body,
            HttpSession session
    ) {
        String username = body.get("username");
        session.setAttribute("webauthn-username", username);
        return svc.beginRegistration(username, session);
    }

    /** 2. Finish registration: consumes the client's attestation response */
    @PostMapping("/register")
    @ResponseBody
    public String registerFinish(
            @RequestBody String bodyJson,
            HttpSession session
    ) throws IOException {
        PublicKeyCredential<
                AuthenticatorAttestationResponse,
                ClientRegistrationExtensionOutputs
                > pk = PublicKeyCredential
                .<AuthenticatorAttestationResponse,ClientRegistrationExtensionOutputs>
                        parseRegistrationResponseJson(bodyJson);

        try {
            svc.finishRegistration(pk, session);
            return "OK";
        } catch (RegistrationFailedException e) {
            return "FAIL: " + e.getMessage();
        }
    }

    /** 3. Start login: returns the PublicKeyCredentialRequestOptions JSON */
    @PostMapping("/login/options")
    @ResponseBody
    public PublicKeyCredentialRequestOptions authOptions(
            @RequestBody Map<String, String> body,
            HttpSession session
    ) {
        String username = body.get("username");
        return svc.beginAuthentication(username, session);
    }

    /** 4. Finish login: consumes the client's assertion response */
    @PostMapping("/login")
    @ResponseBody
    public String authFinish(
            @RequestBody String bodyJson,
            HttpSession session
    ) throws IOException {
        PublicKeyCredential<
                AuthenticatorAssertionResponse,
                ClientAssertionExtensionOutputs
                > pk = PublicKeyCredential
                .<AuthenticatorAssertionResponse,ClientAssertionExtensionOutputs>
                        parseAssertionResponseJson(bodyJson);

        try {
            AssertionResult result = svc.finishAuthentication(pk, session);
            return result.isSuccess() ? "OK" : "FAIL";
        } catch (AssertionFailedException e) {
            return "FAIL: " + e.getMessage();
        }
    }

}
