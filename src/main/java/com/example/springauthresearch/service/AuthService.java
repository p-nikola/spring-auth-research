package com.example.springauthresearch.service;


import com.example.springauthresearch.model.User;

public interface AuthService {

    User login(String username, String password);

}
