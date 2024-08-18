package com.alok.email.parser.impl;

import com.alok.email.model.impl.TransactionEmail;
import com.alok.email.parser.Parser;
import com.alok.email.parser.ParserUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class TransactionParser implements Parser {

    @Getter
    private static TransactionParser instance = new TransactionParser();

    @Override
    public TransactionEmail parseEmail(final String senderEmail, final String subject, final String content) {
        try {
            return TransactionEmail.builder()
                    .id(UUID.randomUUID().toString())
                    .subject(subject)
                    .email(senderEmail)
                    .content(content)
                    .bank(ParserUtils.getTransactionType(senderEmail, subject).name())
                    .amount(ParserUtils.getAmountRetriever(senderEmail, subject).retrieve(content))
                    .description(ParserUtils.getRemarksRetriever(senderEmail, subject).retrieve(content))
                    .timestamp(ParserUtils.getTransactionDateRetriever(senderEmail, subject).retrieve(content))
                    .build();
        } catch (Exception ex) {
            // TODO: persist senderEmail/subject/content for future reference.
            // the same can be used to reprocessed again after fixing the issue.
            ex.printStackTrace();
            log.error(ex.getMessage());
        }

        return TransactionEmail.builder()
                .id(UUID.randomUUID().toString())
                .subject(subject)
                .email(senderEmail)
                .content(content)
                .bank(ParserUtils.getTransactionType(senderEmail, subject).name())
                .build();
    }
}
