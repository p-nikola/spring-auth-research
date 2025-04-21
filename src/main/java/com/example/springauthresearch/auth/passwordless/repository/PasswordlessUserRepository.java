package com.example.springauthresearch.auth.passwordless.repository;

import com.example.springauthresearch.auth.passwordless.model.PasswordlessUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordlessUserRepository extends JpaRepository<PasswordlessUser, String> {
}