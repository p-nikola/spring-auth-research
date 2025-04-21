package com.example.springauthresearch.auth.passwordless.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "passwordless_users")
@Getter @Setter
public class PasswordlessUser {
    @Id
    private String email;
    private Instant createdAt = Instant.now();

    public PasswordlessUser(String email) {
        this.email = email;
    }

    public PasswordlessUser() {
    }
}
