package com.alok.home.email;

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
public class EmailServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailServiceApplication.class, args);
	}

}
