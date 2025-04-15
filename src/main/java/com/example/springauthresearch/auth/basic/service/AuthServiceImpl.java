package com.example.springauthresearch.auth.basic.service;

import com.example.springauthresearch.common.model.User;
import com.example.springauthresearch.common.model.exceptions.InvalidArgumentsException;
import com.example.springauthresearch.common.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(String username, String password) {
        // Check if the username and password are not null or empty
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidArgumentsException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidArgumentsException();
        }

        return user;
    }
}
