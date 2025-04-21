package com.example.springauthresearch.auth.passwordless.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This UDS is used *only* for the OTT flow.
 */
@Service("passwordlessUserDetailsService")
public class PasswordlessUserDetailsService implements UserDetailsService {
    private final PasswordlessUserService userService;

    public PasswordlessUserDetailsService(PasswordlessUserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        // will throw if absent
        userService.findOrSaveUser(email);
        return User.withUsername(email)
                .password("")              // no password
                .authorities("ROLE_USER")  // or whatever
                .build();
    }
}
