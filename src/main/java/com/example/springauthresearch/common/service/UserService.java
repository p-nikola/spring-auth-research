package com.example.springauthresearch.common.service;

import com.example.springauthresearch.common.model.enumerations.Role;
import com.example.springauthresearch.common.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User register(String username, String password, String repeatPassword, Role role);
}
