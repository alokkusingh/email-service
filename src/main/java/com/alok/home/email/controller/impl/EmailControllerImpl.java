package com.alok.home.email.controller.impl;

import com.alok.home.commons.dto.EmailRequest;
import com.alok.home.email.controller.EmailController;
import com.alok.home.email.service.EmailService;
import com.alok.home.email.service.HomeStackApiService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class EmailControllerImpl extends EmailController {

    private final EmailService emailService;
    private final HomeStackApiService homeStackApiService;

    public EmailControllerImpl(EmailService emailService, HomeStackApiService homeStackApiService) {
        this.emailService = emailService;
        this.homeStackApiService = homeStackApiService;
    }

    public ResponseEntity<Void> sendEmail(
            String to,
            String subject,
            String body
    ) {
        emailService.sendSimpleMessage(to, subject, body);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> sendEmail(EmailRequest emailRequest) {

        emailService.sendSimpleMessage(emailRequest.to(), emailRequest.subject(), emailRequest.body());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> sendHtmlEmail(EmailRequest emailRequest) throws MessagingException {


        Map<String, Object> thymeleafModel = homeStackApiService.getInvestmentReportDataForCurrentMonthAndPreviousMonth(YearMonth.now());
        thymeleafModel.put("name", "Alok Singh");
        thymeleafModel.put("heading", "This is investment report for Current and previous Month");

        emailService.sendHtmlMessage(emailRequest.to(), emailRequest.subject(), "template-thymeleaf-investment-report.html", thymeleafModel);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> sendDailyReport(EmailRequest emailRequest) throws MessagingException {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        Map<String, Object> thymeleafModel = homeStackApiService.getDailySummaryReportData();

        thymeleafModel.put("heading", "This is Daily Report for " + LocalDate.now().minusDays(1).format(dateTimeFormatter) + ".");

        thymeleafModel.put("name", "Sir");
        emailService.sendHtmlMessage(emailRequest.to(), "Daily Report - " + LocalDate.now(), "template-thymeleaf-daily-report.html", thymeleafModel);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> yearExpenseReport(EmailRequest emailRequest) throws MessagingException {

        int year = LocalDate.now().minusDays(15).getYear();

        Map<String, Object> thymeleafModel = homeStackApiService.getYearlyExpenseReportDataForYear(year);

        thymeleafModel.put("heading", "Yearly Expense Report for " + year + ".");

        thymeleafModel.put("name", "Sir");
        emailService.sendHtmlMessage(emailRequest.to(), "Yearly Report - " + year, "template-thymeleaf-yearly-expense-report.html", thymeleafModel);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> sendInvestmentReport(EmailRequest emailRequest) throws MessagingException {

        Map<String, Object> thymeleafModel = homeStackApiService.getInvestmentReportDataForCurrentMonthAndPreviousMonth(YearMonth.now());

        thymeleafModel.put("heading", "Please find investment report as follows.");
        thymeleafModel.put("name", "Sir");
        emailService.sendHtmlMessage(emailRequest.to(), "Investment Report - " + LocalDate.now(), "template-thymeleaf-investment-report.html", thymeleafModel);

        return ResponseEntity.ok().build();
    }
}
