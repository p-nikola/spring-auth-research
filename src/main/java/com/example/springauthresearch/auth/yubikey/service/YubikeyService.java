package com.example.springauthresearch.auth.yubikey.service;

import com.example.springauthresearch.auth.yubikey.model.YubikeyCredential;
import com.example.springauthresearch.auth.yubikey.repository.YubikeyCredentialRepository;
import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service("yubiUserDetailsService")
public class YubikeyService implements UserDetailsService {
    private final RelyingParty rp;
    private final YubikeyCredentialRepository yubikeyCredentialRepository;

    public YubikeyService(RelyingParty rp, YubikeyCredentialRepository yubikeyCredentialRepository) {
        this.rp = rp;
        this.yubikeyCredentialRepository = yubikeyCredentialRepository;
    }

    public PublicKeyCredentialCreationOptions beginRegistration(String username, HttpSession session) {

        UserIdentity user = UserIdentity.builder().name(username).displayName(username).id(new ByteArray(username.getBytes(StandardCharsets.UTF_8))).build();

        StartRegistrationOptions opts = StartRegistrationOptions.builder().user(user).build();

        PublicKeyCredentialCreationOptions request = rp.startRegistration(opts);
        session.setAttribute("webauthn-registration", request);
        return request;
    }

    public RegistrationResult finishRegistration(PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> credential, HttpSession session) throws RegistrationFailedException {
        PublicKeyCredentialCreationOptions request = (PublicKeyCredentialCreationOptions) session.getAttribute("webauthn-registration");
        String username = (String) session.getAttribute("webauthn-username");

        RegistrationResult result = rp.finishRegistration(FinishRegistrationOptions.builder().request(request).response(credential).build());

        YubikeyCredential cred = new YubikeyCredential();
        cred.setUsername(username);
        cred.setUserHandle(request.getUser().getId().getBytes());
        cred.setCredentialId(result.getKeyId().getId().getBytes());
        cred.setPublicKeyCose(result.getPublicKeyCose().getBytes());
        cred.setSignatureCount(result.getSignatureCount());
        yubikeyCredentialRepository.save(cred);

        return result;
    }


    public PublicKeyCredentialRequestOptions beginAuthentication(String username, HttpSession session) {

        StartAssertionOptions opts = StartAssertionOptions.builder().username(Optional.of(username)).build();

        AssertionRequest request = rp.startAssertion(opts);
        session.setAttribute("webauthn-authentication", request);
        return request.getPublicKeyCredentialRequestOptions();
    }

    public AssertionResult finishAuthentication(PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> credential, HttpSession session) throws AssertionFailedException {

        AssertionRequest request = (AssertionRequest) session.getAttribute("webauthn-authentication");

        return rp.finishAssertion(FinishAssertionOptions.builder().request(request).response(credential).build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (yubikeyCredentialRepository.findByUsername(username).isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return User.withUsername(username)
                .password("")               // no password
                .authorities("ROLE_USER")   // assign roles
                .build();
    }
}
