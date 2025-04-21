package com.example.springauthresearch.common.service;

import com.example.springauthresearch.common.model.enumerations.Role;
import com.example.springauthresearch.common.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    User register(String username, String password, String repeatPassword,String email, Role role);

    void updateUser(User user);

    public Optional<User> findByEmail(String email);
}
