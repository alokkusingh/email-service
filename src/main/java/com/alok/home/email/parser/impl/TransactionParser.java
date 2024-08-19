package com.alok.home.email.parser.impl;

import com.alok.home.email.entity.Email;
import com.alok.home.email.entity.impl.FailedEmail;
import com.alok.home.email.entity.impl.TransactionEmail;
import com.alok.home.email.parser.Parser;
import com.alok.home.email.parser.ParserUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class TransactionParser implements Parser {

    @Getter
    private static TransactionParser instance = new TransactionParser();

    @Override
    public Email parseEmail(final String senderEmail, final String subject, final String content) {
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
            ex.printStackTrace();
            log.error(ex.getMessage());

            return FailedEmail.builder()
                    .id(UUID.randomUUID().toString())
                    .subject(subject)
                    .email(senderEmail)
                    .content(content)
                    .error(ex.getMessage())
                    .build();
        }
    }
}
