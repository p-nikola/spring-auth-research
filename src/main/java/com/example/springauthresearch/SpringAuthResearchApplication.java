package com.example.springauthresearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.ott.InMemoryOneTimeTokenService;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan
public class SpringAuthResearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAuthResearchApplication.class, args);
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public JavaMailSender javaMailSender(){
        return new JavaMailSenderImpl();

    }

    //@Bean
    public OneTimeTokenService oneTimeTokenService() {
        return new InMemoryOneTimeTokenService();
    }




}
