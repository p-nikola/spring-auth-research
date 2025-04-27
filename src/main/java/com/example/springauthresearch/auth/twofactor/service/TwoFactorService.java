package com.example.springauthresearch.auth.twofactor.service;

import com.example.springauthresearch.common.model.User;
import com.example.springauthresearch.common.service.UserService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.stereotype.Service;

@Service
public class TwoFactorService {
    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();
    private final UserService userService;

    public TwoFactorService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Generate & persist a new secret for the user,
     * and return the full GoogleAuthenticatorKey
     */
    public GoogleAuthenticatorKey generateKey(String username) {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        User u = (User) userService.loadUserByUsername(username);
        u.setTwoFactorSecret(key.getKey());
        userService.updateUser(u);
        return key;
    }

    /**
     * Verify a 6‑digit TOTP code
     */
    public boolean verifyCode(String username, int code) {
        User u = (User) userService.loadUserByUsername(username);
        return gAuth.authorize(u.getTwoFactorSecret(), code);
    }

    public void enableTwoFactor(String username) {
        User u = (User) userService.loadUserByUsername(username);
        u.setTwoFactorEnabled(true);
        userService.updateUser(u);

    }


    public void disableTwoFactor(String username) {
        User u = (User) userService.loadUserByUsername(username);
        u.setTwoFactorEnabled(false);
        userService.updateUser(u);

    }
}
