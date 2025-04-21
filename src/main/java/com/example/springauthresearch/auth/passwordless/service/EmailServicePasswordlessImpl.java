package com.example.springauthresearch.auth.passwordless.service;

import com.example.springauthresearch.common.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service("passwordlessemail")
public class EmailServicePasswordlessImpl implements EmailService {
    private final JavaMailSender mailSender;



    public EmailServicePasswordlessImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;

        if (mailSender instanceof JavaMailSenderImpl impl) {
            System.out.println("[MAIL CONFIG FOR PASSWORDLESS LOGIN] host=" + impl.getHost()
                    + " port=" + impl.getPort()
                    + " username=" + impl.getUsername());
        }
    }


    @Override
    public void sendResetPasswordEmail(String to, String magicLink) {


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Passwordless login link");
        message.setText("Click the following link to login:\n" + magicLink);
        message.setFrom("johndevo1337@gmail.com");

        mailSender.send(message);
    }
}
