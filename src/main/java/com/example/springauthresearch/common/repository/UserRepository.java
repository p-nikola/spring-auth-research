package com.example.springauthresearch.common.repository;

import com.example.springauthresearch.common.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findByEmail(String email);
}
