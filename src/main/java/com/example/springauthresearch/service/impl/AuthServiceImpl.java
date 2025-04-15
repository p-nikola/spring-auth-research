package com.example.springauthresearch.service.impl;

import com.example.springauthresearch.model.User;
import com.example.springauthresearch.model.exceptions.InvalidArgumentsException;
import com.example.springauthresearch.repository.UserRepository;
import com.example.springauthresearch.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        // Check if the username and password are not null or empty
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        return userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(InvalidArgumentsException::new);
    }
}
