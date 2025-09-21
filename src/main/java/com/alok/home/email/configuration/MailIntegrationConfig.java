package com.alok.home.email.configuration;

import com.alok.home.email.entity.Email;
import com.alok.home.email.entity.impl.FailedEmail;
import com.alok.home.email.entity.impl.NoContentEmail;
import com.alok.home.email.entity.impl.StatementEmail;
import com.alok.home.email.entity.impl.TransactionEmail;
import com.alok.home.email.properties.EmailProperties;
import com.alok.home.email.repository.FailedEmailRepository;
import com.alok.home.email.repository.TransactionEmailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mail.dsl.Mail;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;

@Slf4j
@EnableIntegration
@Configuration
public class MailIntegrationConfig {

    private final String SUBJECT_KEYWORDS = "Home Stack Email Test";

    @Bean
    public IntegrationFlow mailIntegration(
            EmailProperties props,
            EmailTransformer emailTransformer,
            TransactionEmailRepository transactionEmailRepository,
            FailedEmailRepository failedEmailRepository
    ) {
        return IntegrationFlow
                .from(
                        Mail.imapInboundAdapter(props.getImapUrl())
                                .maxFetchSize(1)
                                .shouldDeleteMessages(false)
                                .simpleContent(true)
                                .shouldMarkMessagesAsRead(false)
                                .autoCloseFolder(false),
                        e -> e.poller(
                                Pollers.fixedDelay(props.getPollRate())
                        )
                )
                .<Message>filter(message -> {
                    boolean containsKeyword = false;
                    try {
                        containsKeyword = message.getSubject().toUpperCase().contains(SUBJECT_KEYWORDS.toUpperCase());
                    } catch (MessagingException e1) {
                        e1.printStackTrace();
                    }

                    // TODO: allow all mail
                    return true;
                })
                .transform(emailTransformer)
                .handle(message -> {
                    IO.println("New message arrived: " + message);
                    log.info("New message arrived: {}", message);
                    switch (message.getPayload()) {
                        case TransactionEmail email -> {
                            log.info(email.toString());
                            transactionEmailRepository.save(email);
                        }
                        case StatementEmail email -> log.info(email.toString());
                        case NoContentEmail email -> log.info(email.toString());
                        case FailedEmail email -> {
                            log.info(email.toString());
                            failedEmailRepository.save(email);
                        }
                        case Email email -> log.info(email.toString());
                        default -> log.info("Unknown payload type: {}", message.getPayload());
                    }
                })
                .get();
    }
}
