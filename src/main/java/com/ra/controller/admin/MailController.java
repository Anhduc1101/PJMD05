package com.ra.controller.admin;

import com.ra.service.mail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {
    @Autowired
    private EmailService emailService;
//    @GetMapping("/test")
//    public ResponseEntity<?> testMail(){
//        emailService.sendMail();
//        return new ResponseEntity<>("OK CHua", HttpStatus.OK);
//    }
}
