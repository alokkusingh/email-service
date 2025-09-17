package com.alok.home.email.configuration;

import com.alok.home.email.service.EmailService;
import com.alok.home.email.service.HomeStackApiService;
import jakarta.mail.MessagingException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@EnableScheduling
@Configuration
public class SchedulerConfig {

    private final EmailService emailService;
    private final HomeStackApiService homeStackApiService;

    public SchedulerConfig(EmailService emailService, HomeStackApiService homeStackApiService) {
        this.emailService = emailService;
        this.homeStackApiService = homeStackApiService;
    }



    @Scheduled(cron = "0 0 6 * * ?", zone = "Asia/Kolkata")
    void scheduleDailyMorning() throws MessagingException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        Map<String, Object> thymeleafModel = homeStackApiService.getDailySummaryReportData();

        thymeleafModel.put("heading", "This is Daily Report for " + LocalDate.now().minusDays(1).format(dateTimeFormatter) + ".");

        thymeleafModel.put("name", "Alok");
        emailService.sendHtmlMessage("alok.ku.singh@gmail.com", "Daily Report - " + LocalDate.now(), "template-thymeleaf-daily-report.html", thymeleafModel);

        thymeleafModel.put("name", "Rachna");
        emailService.sendHtmlMessage("rachna2589@gmail.com", "Daily Report - " + LocalDate.now(), "template-thymeleaf-daily-report.html", thymeleafModel);
    }

    @Scheduled(cron = "0 30 12 * * ?", zone = "Asia/Kolkata")
    void scheduleDailyInvestmentReport() throws MessagingException {

        Map<String, Object> thymeleafModel = homeStackApiService.getInvestmentReportDataForCurrentMonthAndPreviousMonth(YearMonth.now());

        thymeleafModel.put("heading", "Please find investment report as follows.");
        thymeleafModel.put("name", "Alok");
        emailService.sendHtmlMessage("alok.ku.singh@gmail.com", "Investment Report - " + LocalDate.now(), "template-thymeleaf-investment-report.html", thymeleafModel);
    }
}