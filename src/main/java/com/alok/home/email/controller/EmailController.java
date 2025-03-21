package com.alok.home.email.controller;

import com.alok.home.email.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.alok.home.commons.dto.EmailRequest;

@RequestMapping()
@RestController
public class EmailController {

    private EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping(value = "/send")
    public ResponseEntity<Void> sendEmail(
            @RequestParam(name = "to", required = false, defaultValue = "alok.ku.singh@gmail.com") String to,
            @RequestParam(name = "subject", required = false, defaultValue = "Mail from Home Stack") String subject,
            @RequestParam(name = "body", required = false, defaultValue = "Test mail") String body
    ) {

        emailService.sendSimpleMessage(to, subject, body);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/send")
    public ResponseEntity<Void> sendEmailPost(@RequestBody() EmailRequest emailRequest) {

        emailService.sendSimpleMessage(emailRequest.to(), emailRequest.subject(), emailRequest.body());
        return ResponseEntity.ok().build();
    }
}
