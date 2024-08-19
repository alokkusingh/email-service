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
                    System.out.println("New message received: " + message);
                    if (message.getPayload() instanceof TransactionEmail) {
                        TransactionEmail email = (TransactionEmail) message.getPayload();
                        log.info(email.toString());
                        transactionEmailRepository.save(email);
                    } else if (message.getPayload() instanceof StatementEmail) {
                        StatementEmail email = (StatementEmail) message.getPayload();
                        log.info(email.toString());
                    } else if (message.getPayload() instanceof NoContentEmail) {
                        NoContentEmail email = (NoContentEmail) message.getPayload();
                        log.info(email.toString());
                    } else if (message.getPayload() instanceof FailedEmail) {
                        FailedEmail email = (FailedEmail) message.getPayload();
                        log.info(email.toString());
                        failedEmailRepository.save(email);
                    } else {
                        Email email = (Email) message.getPayload();
                        log.info(email.toString());
                    }
                })
                .get();
    }
}
