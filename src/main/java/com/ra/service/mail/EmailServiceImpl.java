package com.ra.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    public String sendMail() {
        try{
            SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
            simpleMailMessage.setFrom("jav2306@gmail.com");
            simpleMailMessage.setTo("nguyenanhduc1101@gmail.com");
            simpleMailMessage.setText("thank you");
            simpleMailMessage.setSubject("cua hang tap hoa online");
            javaMailSender.send(simpleMailMessage);
            return "da gui xong";
        }catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }
}
