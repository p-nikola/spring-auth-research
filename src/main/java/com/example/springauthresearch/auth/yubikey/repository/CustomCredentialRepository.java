package com.example.springauthresearch.auth.yubikey.repository;

import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.*;
import org.springframework.stereotype.Repository;
import com.example.springauthresearch.auth.yubikey.model.YubikeyCredential;

import java.util.*;

@Repository
public class CustomCredentialRepository implements CredentialRepository {
    private final YubikeyCredentialRepository repo;

    public CustomCredentialRepository(YubikeyCredentialRepository repo) {
        this.repo = repo;
    }

    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        List<YubikeyCredential> creds = repo.findByUsername(username);
        Set<PublicKeyCredentialDescriptor> out = new HashSet<>();
        creds.forEach(c ->
                out.add(PublicKeyCredentialDescriptor.builder()
                        .id(new ByteArray(c.getCredentialId()))
                        .build())
        );
        return out;
    }

    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        return repo.findByCredentialId(credentialId.getBytes()).map(c ->
                RegisteredCredential.builder()
                        .credentialId(new ByteArray(c.getCredentialId()))
                        .userHandle(new ByteArray(c.getUserHandle()))
                        .publicKeyCose(new ByteArray(c.getPublicKeyCose()))
                        .signatureCount(c.getSignatureCount())
                        .build()
        );
    }

    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
        return lookup(credentialId, null)
                .map(Set::of)
                .orElseGet(Set::of);
    }

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        return repo.findByUsername(username).stream()
                .findFirst()
                .map(c -> new ByteArray(c.getUserHandle()));
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        return repo.findByUserHandle(userHandle.getBytes())
                .map(YubikeyCredential::getUsername);
    }
}
