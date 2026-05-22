package com.alok.home.email;

import com.alok.home.email.service.EmailService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan({
		"com.alok.home.email.properties",
		"com.alok.home.commons.security.properties"
})
@SpringBootApplication(
		scanBasePackages = {
				"com.alok.home.email",
				"com.alok.home.commons.exception",
				"com.alok.home.commons.security"
		}
)
public class EmailServiceApplication implements ApplicationRunner {

    private final EmailService emailService;

    public EmailServiceApplication(EmailService emailService) {
        this.emailService = emailService;
    }

    public static void main(String[] args) {
		SpringApplication.run(EmailServiceApplication.class, args);
	}

    @Override
    public void run(ApplicationArguments args) throws Exception {
        emailService.sendSimpleMessage(
                "alok.ku.singh@gmail.com",
                "HOME-STACK - Email Service is up and running",
                "Hello Alok,\n\nThis is a notification to inform you that the Email Service of your HOME-STACK application is now up and running successfully.\n\nBest regards"
        );
    }
}
