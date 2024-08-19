package com.alok.home.email.parser.factory.impl;

import com.alok.home.email.parser.impl.TransactionParser;
import com.alok.home.email.parser.factory.EmailParserFactory;
import lombok.Getter;

public class EmailTransactionParserFactory implements EmailParserFactory {

    @Getter
    private static EmailTransactionParserFactory instance = new EmailTransactionParserFactory();

    @Override
    public TransactionParser getParser() {
        return TransactionParser.getInstance();
    }
}
