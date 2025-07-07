package com.todoapp.todoapp.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PasswordResetUtil {

    private final JavaMailSender mailSender;

    public PasswordResetUtil(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String generateOtp(){
        return String.format("%06d", new Random().nextInt(999999));
    }

    public void sendEmail(String to, String otp){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Şifre Sıfırlama Kodu");
        message.setText("Şifre sıfırlama kodunuz: " + otp + "\n10 Dakika İçerisinde Kullanınız!");
        mailSender.send(message);
    }
}
