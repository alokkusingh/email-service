package com.alok.home.email.controller;

import com.alok.home.email.service.EmailService;
import com.alok.home.email.service.HomeStackApiService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.alok.home.commons.dto.EmailRequest;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Collections;
import java.util.Map;

@RequestMapping()
@RestController
public class EmailController {

    private EmailService emailService;
    private HomeStackApiService homeStackApiService;

    public EmailController(EmailService emailService, HomeStackApiService homeStackApiService) {
        this.emailService = emailService;
        this.homeStackApiService = homeStackApiService;
    }

    @GetMapping(value = "/send")
    public ResponseEntity<Void> sendEmail(
            @RequestParam(name = "name", required = false, defaultValue = "alok.ku.singh@gmail.com") String to,
            @RequestParam(name = "subject", required = false, defaultValue = "Mail from Home Stack") String subject,
            @RequestParam(name = "body", required = false, defaultValue = "Test mail") String body
    ) {

        emailService.sendSimpleMessage(to, subject, body);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/send")
    public ResponseEntity<Void> sendEmail(@RequestBody() EmailRequest emailRequest) {

        emailService.sendSimpleMessage(emailRequest.to(), emailRequest.subject(), emailRequest.body());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/sendHtml")
    public ResponseEntity<Void> sendHtmlEmail(@RequestBody() EmailRequest emailRequest) throws MessagingException {


        Map<String, Object> thymeleafModel = homeStackApiService.getInvestmentReportDataForCurrentMonthAndPreviousMonth(YearMonth.now());
        thymeleafModel.put("name", "Alok Singh");
        thymeleafModel.put("heading", "This is investment report for Current and previous Month");

        emailService.sendHtmlMessage(emailRequest.to(), emailRequest.subject(), "template-thymeleaf-investment-report.html", thymeleafModel);
        return ResponseEntity.ok().build();
    }
}
