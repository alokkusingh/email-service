package com.alok.home.email.controller;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.alok.home.commons.dto.EmailRequest;

@RequestMapping("/send")
@RestController
public abstract class EmailController {

    @GetMapping()
    public abstract ResponseEntity<Void> sendEmail(
            @RequestParam(name = "name", required = false, defaultValue = "alok.ku.singh@gmail.com") String to,
            @RequestParam(name = "subject", required = false, defaultValue = "Mail from Home Stack") String subject,
            @RequestParam(name = "body", required = false, defaultValue = "Test mail") String body
    );

    @PostMapping()
    public abstract ResponseEntity<Void> sendEmail(@RequestBody() EmailRequest emailRequest);

    @PostMapping(value = "/html")
    public abstract ResponseEntity<Void> sendHtmlEmail(@RequestBody() EmailRequest emailRequest) throws MessagingException;

    @PostMapping("/dailyReport")
    public abstract ResponseEntity<Void> sendDailyReport(@RequestBody() EmailRequest emailRequest) throws MessagingException;

    @PostMapping("/yearExpenseReport")
    public abstract ResponseEntity<Void> yearExpenseReport(@RequestBody() EmailRequest emailRequest) throws MessagingException;

    @PostMapping("/investmentReport")
    public abstract ResponseEntity<Void> sendInvestmentReport(@RequestBody() EmailRequest emailRequest) throws MessagingException;
}
