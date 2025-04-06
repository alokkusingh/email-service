package com.alok.home.email.configuration;

import com.alok.home.email.entity.Email;
import com.alok.home.email.entity.impl.FailedEmail;
import com.alok.home.email.entity.impl.NoContentEmail;
import com.alok.home.email.entity.impl.StatementEmail;
import com.alok.home.email.entity.impl.TransactionEmail;
import com.alok.home.email.properties.DatabaseProperties;
import com.alok.home.email.repository.FailedEmailRepository;
import com.alok.home.email.repository.TransactionEmailRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.jpa.dsl.Jpa;

@Slf4j
@EnableIntegration
@Configuration
public class DBFailedMailIntegrationConfig {

    @Bean
    public IntegrationFlow dbFailedMailIntegration(
            DatabaseProperties props,
            FailedEmailTransformer failedEmailTransformer,
            TransactionEmailRepository transactionEmailRepository,
            FailedEmailRepository failedEmailRepository,
            EntityManagerFactory entityManagerFactory
    ) {
        return IntegrationFlow
                .from(Jpa.inboundAdapter(entityManagerFactory)
                                .entityClass(FailedEmail.class)
                                .jpaQuery("select e from FailedEmail e where e.processed = false")
                                .maxResults(1)
                                .expectSingleResult(true),
                        e -> e.poller(Pollers.fixedDelay(props.getPollRate())
                        )
                )
                .transform(failedEmailTransformer)
                .handle(message -> {
                    System.out.println("New message received: " + message);
                    if (message.getPayload() instanceof TransactionEmail email) {
                        log.info(email.toString());
                        transactionEmailRepository.save(email);
                        failedEmailRepository.updateProcessedById(email.getId(), true, true);
                    } else if (message.getPayload() instanceof StatementEmail email) {
                        log.info(email.toString());
                    } else if (message.getPayload() instanceof NoContentEmail email) {
                        log.info(email.toString());
                    } else if (message.getPayload() instanceof FailedEmail email) {
                        log.info(email.toString());
                        failedEmailRepository.updateProcessedById(email.getId(), true, false);
                    } else {
                        Email email = (Email) message.getPayload();
                        log.info(email.toString());
                    }
                })
                .get();
    }
}
