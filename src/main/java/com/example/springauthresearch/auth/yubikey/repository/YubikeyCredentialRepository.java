package com.example.springauthresearch.auth.yubikey.repository;

import com.example.springauthresearch.auth.yubikey.model.YubikeyCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.*;

public interface YubikeyCredentialRepository extends JpaRepository<YubikeyCredential,Long> {
    List<YubikeyCredential> findByUsername(String username);

    @Query("SELECT c FROM YubikeyCredential c WHERE c.credentialId = :credentialId")
    Optional<YubikeyCredential> findByCredentialId(byte[] credentialId);

    @Query("SELECT c FROM YubikeyCredential c WHERE c.userHandle = :userHandle")
    Optional<YubikeyCredential> findByUserHandle(byte[] userHandle);
}
