package com.example.springauthresearch.common.service;

public interface EmailService {

    void sendResetPasswordEmail(String to, String token);


}
