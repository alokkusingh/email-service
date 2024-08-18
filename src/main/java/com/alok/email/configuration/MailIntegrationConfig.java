package com.alok.email.configuration;

import com.alok.email.model.Email;
import com.alok.email.model.impl.NoContentEmail;
import com.alok.email.model.impl.StatementEmail;
import com.alok.email.model.impl.TransactionEmail;
import com.alok.email.properties.EmailProperties;
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
            EmailTransformer emailTransformer
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
                        System.out.println(email);
                    } else if (message.getPayload() instanceof StatementEmail) {
                        StatementEmail email = (StatementEmail) message.getPayload();
                        System.out.println(email);
                    } else if (message.getPayload() instanceof NoContentEmail) {
                        NoContentEmail email = (NoContentEmail) message.getPayload();
                        System.out.println(email);
                    } else {
                        Email email = (Email) message.getPayload();
                        System.out.println(email);
                    }
                })
                .get();
    }
}
