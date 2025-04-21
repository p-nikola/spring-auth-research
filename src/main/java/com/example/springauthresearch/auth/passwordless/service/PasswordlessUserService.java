package com.example.springauthresearch.auth.passwordless.service;

import com.example.springauthresearch.auth.passwordless.model.PasswordlessUser;
import com.example.springauthresearch.auth.passwordless.repository.PasswordlessUserRepository;
import org.springframework.stereotype.Service;

@Service
public class PasswordlessUserService {
    private final PasswordlessUserRepository repo;

    public PasswordlessUserService(PasswordlessUserRepository repo) {
        this.repo = repo;
    }

    /**
     * Ensure a user record exists for this email; return the managed entity.
     */
    public PasswordlessUser findOrSaveUser(String email) {
        return repo.findById(email)
                .orElseGet(() -> repo.save(new PasswordlessUser(email)));
    }
}
