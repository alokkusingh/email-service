package com.alok.email.controller;

import com.alok.email.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/email")
@RestController
public class EmailController {

    private EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping(value = "/send")
    public ResponseEntity<Void> sendEmail() {

        emailService.sendSimpleMessage("a202.jyothigt@gmail.com", "Mail from Home Stack", "This is first mail");

        return ResponseEntity.ok().build();
    }
}
