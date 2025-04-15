package com.example.springauthresearch.service;

import com.example.springauthresearch.model.User;
import com.example.springauthresearch.model.enumerations.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User register(String username, String password, String repeatPassword, String name, String surname, Role role);
}
