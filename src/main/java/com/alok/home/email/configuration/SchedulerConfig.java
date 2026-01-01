package com.alok.home.email.configuration;

import com.alok.home.email.service.EmailService;
import com.alok.home.email.service.HomeStackApiService;
import jakarta.mail.MessagingException;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
@Configuration
public class SchedulerConfig {

    private final EmailService emailService;
    private final HomeStackApiService homeStackApiService;

    public SchedulerConfig(EmailService emailService, HomeStackApiService homeStackApiService) {
        this.emailService = emailService;
        this.homeStackApiService = homeStackApiService;
    }

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(dataSource);
    }

    @Scheduled(cron = "0 0 6 * * ?", zone = "Asia/Kolkata")
    @SchedulerLock(name = "dailyMorningReportLock", lockAtLeastFor = "PT4M", lockAtMostFor = "PT14M")
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
    @SchedulerLock(name = "dailyInvestmentReportLock", lockAtLeastFor = "PT4M", lockAtMostFor = "PT14M")
    void scheduleDailyInvestmentReport() throws MessagingException {

        Map<String, Object> thymeleafModel = homeStackApiService.getInvestmentReportDataForCurrentMonthAndPreviousMonth(YearMonth.now());

        thymeleafModel.put("heading", "Please find investment report as follows.");
        thymeleafModel.put("name", "Alok");
        emailService.sendHtmlMessage("alok.ku.singh@gmail.com", "Investment Report - " + LocalDate.now(), "template-thymeleaf-investment-report.html", thymeleafModel);
    }

    // Every 1st day of month at 9:00 AM
    @Scheduled(cron = "0 0 9 1 * ?", zone = "Asia/Kolkata")
    @SchedulerLock(name = "monthlyYearExpenseReport", lockAtLeastFor = "PT4M", lockAtMostFor = "PT14M")
    void scheduleMonthlyYearExpenseReport() throws MessagingException {

        int year = LocalDate.now().minusDays(15).getYear();

        Map<String, Object> thymeleafModel = homeStackApiService.getYearlyExpenseReportDataForYear(year);

        thymeleafModel.put("heading", "Yearly Expense Report for " + year + ".");

        thymeleafModel.put("name", "Alok");
        emailService.sendHtmlMessage("alok.ku.singh@gmail.com", "Year Report - " + year, "template-thymeleaf-yearly-expense-report.html", thymeleafModel);

        thymeleafModel.put("name", "Rachna");
        emailService.sendHtmlMessage("rachna2589@gmail.com", "Year Report - " + year, "template-thymeleaf-yearly-expense-report.html", thymeleafModel);
    }
}