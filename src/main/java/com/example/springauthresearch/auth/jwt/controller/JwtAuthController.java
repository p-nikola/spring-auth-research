package com.example.springauthresearch.auth.jwt.controller;

import com.example.springauthresearch.auth.jwt.service.JwtService;
import com.example.springauthresearch.common.dto.LoginRequest;
import com.example.springauthresearch.common.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/jwt")
public class JwtAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;


    public JwtAuthController(AuthenticationConfiguration authenticationManager, JwtService jwtService, UserService userService) throws Exception {
        this.authenticationManager = authenticationManager.getAuthenticationManager();
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        var user = userService.loadUserByUsername(request.getUsername());
        var jwt = jwtService.generateToken(user);

        //Set cookie
        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60)
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok("OK");
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .path("/")
                .maxAge(0)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Logged out");
    }

    @GetMapping("/token")
    public ResponseEntity<String> exposeJwt(@CookieValue("jwt") String jwt) {
        return ResponseEntity.ok(jwt);
    }




    @GetMapping("/test")
    public ResponseEntity<String> testSecured() {
        return ResponseEntity.ok("JWT is valid");
    }
}
