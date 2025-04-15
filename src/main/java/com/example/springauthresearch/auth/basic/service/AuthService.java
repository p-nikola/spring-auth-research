package com.example.springauthresearch.auth.basic.service;


import com.example.springauthresearch.common.model.User;

public interface AuthService {

    User login(String username, String password);

}
