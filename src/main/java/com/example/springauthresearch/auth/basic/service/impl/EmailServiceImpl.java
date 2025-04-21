package com.example.springauthresearch.auth.basic.service.impl;

import com.example.springauthresearch.common.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service("emailservicebasic")
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;

        if (mailSender instanceof JavaMailSenderImpl impl) {
            System.out.println("[MAIL CONFIG] host=" + impl.getHost()
                    + " port=" + impl.getPort()
                    + " username=" + impl.getUsername());
        }
    }


    @Override
    public void sendResetPasswordEmail(String to, String token) {
        String resetUrl = "http://localhost:9090/auth/basic/password/reset?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("Click the following link to reset your password:\n" + resetUrl);
        message.setFrom("johndevo1337@gmail.com");

        mailSender.send(message);
    }
}
