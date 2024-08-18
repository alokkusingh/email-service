package com.alok.email.parser.impl;

import com.alok.email.model.impl.TransactionEmail;
import com.alok.email.parser.Parser;
import com.alok.email.parser.ParserUtils;
import lombok.Getter;

public class TransactionParser implements Parser {

    @Getter
    private static TransactionParser instance = new TransactionParser();

    @Override
    public TransactionEmail parseEmail(final String senderEmail, final String subject, final String content) {
        return TransactionEmail.builder()
                .subject(subject)
                .email(senderEmail)
                .content(content)
                .amount(ParserUtils.getAmountRetriever(senderEmail, subject).retrieve(content))
                .description(ParserUtils.getRemarksRetriever(senderEmail, subject).retrieve(content))
                .timestamp(ParserUtils.getTransactionDateRetriever(senderEmail, subject).retrieve(content))
                .build();
    }
}
